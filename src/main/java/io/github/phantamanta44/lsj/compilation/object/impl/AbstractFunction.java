package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IFunction;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.compilation.object.Type;
import io.github.phantamanta44.lsj.execution.ExecutionContext;
import io.github.phantamanta44.lsj.execution.exception.ParamCountIE;

import java.util.List;

public abstract class AbstractFunction<T extends AbstractFunction<T, R>, R extends IValue> implements IFunction<T, R> {

    private final String name;
    private final Type<R> returnType;
    private final Type<T> type;

    @SuppressWarnings("unchecked")
    public AbstractFunction(String name, Type<R> returnType) {
        this.name = name;
        this.returnType= returnType;
        this.type = BuiltIns.<T, R>closureType(returnType);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type<R> getReturnType() {
        return returnType;
    }

    @Override
    public Type<T> getType() {
        return type;
    }

    @Override
    public T resolve(ExecutionContext ctx) throws InterpretationException {
        return null;
    }

    protected void checkArgCount(List<IExpression<?>> args, int count) throws InterpretationException {
        if (args.size() != count) throw new ParamCountIE(this, count, args.size());
    }

}