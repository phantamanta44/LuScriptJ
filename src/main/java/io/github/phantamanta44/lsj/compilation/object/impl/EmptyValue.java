package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public class EmptyValue extends AbstractValue<EmptyValue> {

    public static final EmptyValue EMPTY = new EmptyValue();

    private EmptyValue() {
        super(BuiltIns.T_EMPTY);
    }

    @Override
    public String asDisplayString(ExecutionContext ctx) {
        return "empty";
    }

    @Override
    public boolean isEqualTo(ExecutionContext ctx, IValue<?> other) throws InterpretationException {
        return other.instanceOf(BuiltIns.T_EMPTY);
    }

}
