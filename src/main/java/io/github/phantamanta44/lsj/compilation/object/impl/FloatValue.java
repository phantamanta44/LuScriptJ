package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public class FloatValue extends NumeralValue<FloatValue> {

    public static final double EQUALITY_THRESH = 1e-4D;

    private final double value;
    
    public FloatValue(double value) {
        super(BuiltIns.T_FLOAT);
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    @Override
    public double asDouble() {
        return value;
    }

    @Override
    public String asDisplayString(ExecutionContext ctx) {
        return Double.toString(value);
    }

    @Override
    public boolean isEqualTo(ExecutionContext ctx, IValue<?> other) throws InterpretationException {
        if (other.instanceOf(BuiltIns.T_INT)) {
            return Math.abs(BuiltIns.T_INT.cast(other).getValue() - value) < FloatValue.EQUALITY_THRESH;
        }
        if (other.instanceOf(BuiltIns.T_FLOAT)) {
            return Math.abs(BuiltIns.T_FLOAT.cast(other).value - value) < FloatValue.EQUALITY_THRESH;
        }
        return false;
    }
    
}
