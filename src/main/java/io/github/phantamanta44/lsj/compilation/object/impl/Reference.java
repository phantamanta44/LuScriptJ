package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.ICallable;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.compilation.object.Type;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

public class Reference<T extends IValue<T>> extends AbstractExpression<T> {

    private final String identifier;

    public Reference(String identifier, Type<T> type) {
        super(type);
        this.identifier = identifier;
    }

    @Override
    public T resolve(ExecutionContext ctx) throws InterpretationException {
        return ctx.getObjRegistry().resolve(identifier);
    }

    public static class Function<T extends ICallable<T, R>, R extends IValue> extends Reference<T> {

        @SuppressWarnings("unchecked")
        public Function(String identifier, Type<R> returnType) {
            super(identifier, BuiltIns.closureType(returnType));
        }

    }

}
