package io.github.phantamanta44.lsj.execution.call;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public abstract class RootCall {

    private final int line, pos;

    public RootCall(int line, int pos) {
        this.line = line;
        this.pos = pos;
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

    public abstract IValue<?> performCall(ExecutionContext ctx) throws InterpretationException;

}
