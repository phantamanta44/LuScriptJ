package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.compilation.object.Type;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public abstract class AbstractValue<T extends AbstractValue<T>> implements IValue<T> {

    private final Type<T> type;

    public AbstractValue(Type<T> type) {
        this.type = type;
    }

    @Override
    public Type<T> getType() {
        return type;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T resolve(ExecutionContext ctx) {
        return (T)this;
    }

}
