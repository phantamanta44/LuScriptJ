package io.github.phantamanta44.lsj.compilation.object;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

import java.util.List;

public interface ICallable<T extends ICallable<T, R>, R extends IValue> extends IValue<T> {

    R invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException;

    Type<R> getReturnType();

}
