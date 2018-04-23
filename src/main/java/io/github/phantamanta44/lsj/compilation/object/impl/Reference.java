package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.ICallable;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.compilation.object.Type;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public class Reference<T extends IValue<T>> extends AbstractExpression<T> {

    private final String identifier;
    private final int line, pos;

    public Reference(String identifier, Type<T> type, int line, int pos) {
        super(type);
        this.identifier = identifier;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public T resolve(ExecutionContext ctx) throws InterpretationException {
        try {
            return ctx.getObjRegistry().resolve(identifier);
        } catch (InterpretationException e) {
            throw e.from(line, pos);
        }
    }

    public static class Function<T extends ICallable<T, R>, R extends IValue> extends Reference<T> {

        @SuppressWarnings("unchecked")
        public Function(String identifier, Type<R> returnType, int line, int pos) {
            super(identifier, BuiltIns.closureType(returnType), line, pos);
        }

    }

}
