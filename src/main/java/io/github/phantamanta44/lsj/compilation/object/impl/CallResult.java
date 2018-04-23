package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.ICallable;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IFunction;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

import java.util.List;

public class CallResult<T extends ICallable<T, R>, R extends IValue<R>> extends AbstractExpression<R> {

    private final IExpression<T> function;
    private final List<IExpression<?>> params;
    private final int line, pos;

    public CallResult(IExpression<T> function, List<IExpression<?>> params, int line, int pos) {
        super(BuiltIns.anyType());
        this.function = function;
        this.params = params;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public R resolve(ExecutionContext ctx) throws InterpretationException {
        T func = function.resolve(ctx);
        return func.invoke(ctx, params);
    }

    @Override
    public int getDeclarationLine() {
        return line;
    }

    @Override
    public int getDeclarationPos() {
        return pos;
    }

}
