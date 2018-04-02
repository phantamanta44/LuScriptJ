package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.ICallable;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

import java.util.List;

public class CallResult<T extends ICallable<T, R>, R extends IValue<R>> extends AbstractExpression<R> {

    private final IExpression<T> function;
    private final List<IExpression<?>> params;

    public CallResult(IExpression<T> function, List<IExpression<?>> params) {
        super(BuiltIns.anyType());
        this.function = function;
        this.params = params;
    }

    @Override
    public R resolve(ExecutionContext ctx) throws InterpretationException {
        return function.resolve(ctx).invoke(ctx, params);
    }

}
