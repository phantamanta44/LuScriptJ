package io.github.phantamanta44.lsj.compilation.object;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public interface IExpression<T extends IValue<T>> {

    Type<T> getType();

    T resolve(ExecutionContext ctx) throws InterpretationException;

    default boolean instanceOf(Type<?> type) {
        return getType().isOrIsChildOf(type);
    }

}
