package io.github.phantamanta44.lsj.execution.call;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.object.impl.CallResult;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public class RootFunctionCall extends RootCall {

    private final CallResult<?, ?> call;

    public RootFunctionCall(CallResult<?, ?> call, int line, int pos) {
        super(line, pos);
        this.call = call;
    }

    @Override
    public void performCall(ExecutionContext ctx) throws InterpretationException {
        call.resolve(ctx);
    }

}
