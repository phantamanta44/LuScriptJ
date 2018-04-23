package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.ICallable;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.compilation.object.Type;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

import java.util.LinkedList;
import java.util.List;

public class Closure<T extends Closure<T, R>, R extends IValue<R>>
        extends AbstractValue<Closure<T, R>> implements ICallable<Closure<T, R>, R> {

    private final IExpression<R> expression;
    private final Type<R> returnType;
    private final int line, pos;

    public Closure(IExpression<R> expression, Type<R> returnType, int line, int pos) {
        super(BuiltIns.closureType(returnType));
        this.expression = expression;
        this.returnType = returnType;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public R invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
        String prefix = new String(new char[ctx.getClosureDepth() + 1]).replace("\0", "$");
        List<String> bindings = new LinkedList<>();
        if (!args.isEmpty()) {
            List<IValue<?>> argValues = new LinkedList<>();
            for (IExpression<?> expr : args) argValues.add(expr.resolve(ctx));
            bindings.add(prefix);
            bindings.add(prefix + "0");
            ctx.getObjRegistry().put(prefix, argValues.get(0));
            ctx.getObjRegistry().put(prefix + "0", argValues.get(0));
            for (int i = 1; i < argValues.size(); i++) {
                bindings.add(prefix + i);
                ctx.getObjRegistry().put(prefix + 1, argValues.get(i));
            }
        }
        ctx.incrementClosureDepth();
        R value;
        try {
            value = expression.resolve(ctx);
        } catch (InterpretationException e) {
            throw e.withFrame(null, line, pos);
        }
        ctx.decrementClosureDepth();
        for (String binding : bindings) ctx.getObjRegistry().clear(binding);
        return value;
    }

    @Override
    public Type<R> getReturnType() {
        return returnType;
    }

    @Override
    public String asDisplayString(ExecutionContext ctx) {
        return getType().getName();
    }

}
