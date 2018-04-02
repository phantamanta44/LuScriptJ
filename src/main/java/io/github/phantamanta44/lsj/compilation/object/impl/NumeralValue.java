package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.compilation.object.Type;

public abstract class NumeralValue<T extends NumeralValue<T>> extends AbstractValue<T> {

    public NumeralValue(Type<T> type) {
        super(type);
    }

    public abstract double getAsDouble();
    
}
