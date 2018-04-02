package io.github.phantamanta44.lsj.execution.call;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

@FunctionalInterface
public interface IRootCall {

    void performCall(ExecutionContext ctx) throws InterpretationException;

}
