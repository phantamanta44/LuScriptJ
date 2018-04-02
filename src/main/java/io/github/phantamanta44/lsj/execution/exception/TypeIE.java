package io.github.phantamanta44.lsj.execution.exception;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.object.Type;

public class TypeIE extends InterpretationException {

    public TypeIE(Type expectedType, Type actualType) {
        super(String.format("Type error: expected %s found %s", expectedType, actualType));
    }

}
