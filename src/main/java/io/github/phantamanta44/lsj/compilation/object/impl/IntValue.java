package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public class IntValue extends NumeralValue<IntValue> {

    private final int value;

    public IntValue(int value) {
        super(BuiltIns.T_INT);
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public double getAsDouble() {
        return value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

    @Override
    public boolean isEqualTo(ExecutionContext ctx, IValue<?> other) throws InterpretationException {
        if (other.instanceOf(BuiltIns.T_INT)) {
            return BuiltIns.T_INT.cast(other).value == value;
        }
        if (other.instanceOf(BuiltIns.T_FLOAT)) {
            return Math.abs(BuiltIns.T_FLOAT.cast(other).getValue() - value) < FloatValue.EQUALITY_THRESH;
        }
        return false;
    }

}
