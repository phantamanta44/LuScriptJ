package io.github.phantamanta44.lsj.compilation;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.object.ICallable;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.compilation.object.impl.*;
import io.github.phantamanta44.lsj.execution.call.IRootCall;
import io.github.phantamanta44.lsj.execution.call.RootFunctionCall;
import io.github.phantamanta44.lsj.execution.exception.TypeIE;
import io.github.phantamanta44.lsj.tokenization.model.ICallableNode;
import io.github.phantamanta44.lsj.tokenization.node.*;
import io.github.phantamanta44.lsj.util.Utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Compiler {

    private final List<IRootCall> rootCalls;

    public Compiler() {
        this.rootCalls = new LinkedList<>();
    }

    @SuppressWarnings("unchecked")
    private <T extends ICallable<T, R>, R extends IValue<R>> IExpression<T> produceCallable(ICallableNode node)
            throws InterpretationException {
        if (node instanceof NodeReference) {
            return new Reference.Function<T, R>(((NodeReference)node).identifier, BuiltIns.anyType());
        } else if (node instanceof NodeClosure) {
            IExpression expr = produceExpression(((NodeClosure)node).expression);
            return new Closure<>(expr, expr.getType());
        }
        throw new IllegalStateException("Nonexistent callable node!");
    }

    @SuppressWarnings("unchecked")
    private <T extends ICallable<T, R>, R extends IValue<R>> CallResult<T, R> produceCallResult(NodeFunctionCall node)
            throws InterpretationException {
        List<IExpression<?>> params = new ArrayList<>();
        for (INode paramNode : node.params) params.add(produceExpression(paramNode));
        return new CallResult<>(this.<T, R>produceCallable(node.function), params);
    }

    @SuppressWarnings("unchecked")
    private IExpression<?> produceExpression(INode node) throws InterpretationException {
        if (node instanceof NodeClosure) {
            IExpression expr = produceExpression(((NodeClosure)node).expression);
            return new Closure<>(expr, expr.getType());
        } else if (node instanceof NodeFunctionCall) {
            return produceCallResult((NodeFunctionCall)node);
        } else if (node instanceof NodeLiteralFloat) {
            return new FloatValue(((NodeLiteralFloat)node).value);
        } else if (node instanceof NodeLiteralInt) {
            return new IntValue(((NodeLiteralInt)node).value);
        } else if (node instanceof NodeLiteralList) {
            if (node instanceof NodeLiteralList.Literal) {
                List<INode> values = ((NodeLiteralList.Literal)node).elements;
                if (values.isEmpty()) return new ListValue(null, null);
                ListValue list = new ListValue(produceExpression(values.get(values.size() - 1)), null);
                for (int i = values.size() - 2; i >= 0; i--) {
                    list = new ListValue(produceExpression(values.get(i)), list);
                }
                return list;
            }
            NodeLiteralList.Range range = (NodeLiteralList.Range)node;
            IExpression<?> start = produceExpression(range.start);
            if (!(start.instanceOf(BuiltIns.T_NUMERAL))) throw new TypeIE(BuiltIns.T_NUMERAL, start.getType());
            IExpression<?> end = produceExpression(range.end);
            if (!(end.instanceOf(BuiltIns.T_NUMERAL))) throw new TypeIE(BuiltIns.T_NUMERAL, end.getType());
            if (range.step == null) {
                if (start.instanceOf(BuiltIns.T_INT) && end.instanceOf(BuiltIns.T_INT)) {
                    return new ListComprehension.Int((IExpression<IntValue>)start, (IExpression<IntValue>)end, null);
                }
                return new ListComprehension.Float((IExpression<FloatValue>)start, (IExpression<FloatValue>)end, null);
            } else {
                IExpression<?> step = produceExpression(range.step);
                if (!(step.instanceOf(BuiltIns.T_NUMERAL))) throw new TypeIE(BuiltIns.T_NUMERAL, step.getType());
                if (start.instanceOf(BuiltIns.T_INT)
                        && end.instanceOf(BuiltIns.T_INT)
                        && step.instanceOf(BuiltIns.T_INT)) {
                    return new ListComprehension.Int((IExpression<IntValue>)start,
                            (IExpression<IntValue>)end,
                            (IExpression<IntValue>)step);
                }
                return new ListComprehension.Float((IExpression<FloatValue>)start,
                        (IExpression<FloatValue>)end,
                        (IExpression<FloatValue>)step);
            }
        } else if (node instanceof NodeLiteralString) {
            return new StringValue(Utils.parseEscapes(((NodeLiteralString)node).value));
        } else if (node instanceof NodeReference) {
            return new Reference<>(((NodeReference)node).identifier, BuiltIns.T_ANY);
        }
        throw new IllegalStateException("Nonexistent expression node!");
    }

    public void acceptFunctionCall(NodeFunctionCall node) throws InterpretationException {
        rootCalls.add(new RootFunctionCall(produceCallResult(node)));
    }

    public List<IRootCall> getOutput() {
        return rootCalls;
    }

}
