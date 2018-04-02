package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public class BooleanValue extends AbstractValue<BooleanValue> {

    private final boolean value;

    public BooleanValue(boolean value) {
        super(BuiltIns.T_BOOL);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    @Override
    public boolean isEqualTo(ExecutionContext ctx, IValue<?> other) throws InterpretationException {
        return other.instanceOf(BuiltIns.T_BOOL) && BuiltIns.T_BOOL.cast(other).value == value;
    }

}
