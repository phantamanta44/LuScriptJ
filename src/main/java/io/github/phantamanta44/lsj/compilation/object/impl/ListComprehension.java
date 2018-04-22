package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

import java.util.LinkedList;
import java.util.List;

public abstract class ListComprehension extends AbstractExpression<ListValue> {

    public ListComprehension() {
        super(BuiltIns.T_LIST);
    }

    public static class Int extends ListComprehension {

        private final IExpression<IntValue> start;
        private final IExpression<IntValue> end;
        private final IExpression<IntValue> step;
        
        public Int(IExpression<IntValue> start, IExpression<IntValue> end, IExpression<IntValue> step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        public ListValue resolve(ExecutionContext ctx) throws InterpretationException {
            int iStart = start.resolve(ctx).getValue();
            int iEnd = end.resolve(ctx).getValue();
            int iStep = step != null ? step.resolve(ctx).getValue() : 1;
            List<IExpression<?>> elements = new LinkedList<>();
            for (int i = iStart; i < iEnd; i += iStep) elements.add(new IntValue(i));
            return ListValue.construct(elements); // TODO use a lazy list
        }

    }

    public static class Float extends ListComprehension {

        private final IExpression<FloatValue> start;
        private final IExpression<FloatValue> end;
        private final IExpression<FloatValue> step;

        public Float(IExpression<FloatValue> start, IExpression<FloatValue> end, IExpression<FloatValue> step) {
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        public ListValue resolve(ExecutionContext ctx) throws InterpretationException {
            double fStart = start.resolve(ctx).getValue();
            double fEnd = end.resolve(ctx).getValue();
            double fStep = step != null ? step.resolve(ctx).getValue() : 1D;
            List<IExpression<?>> elements = new LinkedList<>();
            for (double i = fStart; i < fEnd; i += fStep) elements.add(new FloatValue(i));
            return ListValue.construct(elements); // TODO use a lazy list
        }

    }

}
