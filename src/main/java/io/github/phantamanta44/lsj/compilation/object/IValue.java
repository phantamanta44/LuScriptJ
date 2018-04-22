package io.github.phantamanta44.lsj.compilation.object;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public interface IValue<T extends IValue<T>> extends IExpression<T> {

    String asDisplayString(ExecutionContext ctx) throws InterpretationException;

    default boolean isEqualTo(ExecutionContext ctx, IValue<?> other) throws InterpretationException {
        return equals(other);
    }

}
