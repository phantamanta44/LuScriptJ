package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public class StringValue extends AbstractValue<StringValue> {

    private final String value;

    public StringValue(String value) {
        super(BuiltIns.T_STRING);
        this.value = value;
    }

    public StringValue concat(StringValue other) {
        return new StringValue(value + other.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean isEqualTo(ExecutionContext ctx, IValue<?> other) throws InterpretationException {
        return other.instanceOf(BuiltIns.T_STRING) && BuiltIns.T_STRING.cast(other).value.equals(value);
    }

}
