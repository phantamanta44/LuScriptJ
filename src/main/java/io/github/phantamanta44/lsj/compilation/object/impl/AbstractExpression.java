package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.compilation.object.Type;

public abstract class AbstractExpression<T extends IValue<T>> implements IExpression<T> {

    private final Type<T> type;

    public AbstractExpression(Type<T> type) {
        this.type = type;
    }

    @Override
    public Type<T> getType() {
        return type;
    }

}
