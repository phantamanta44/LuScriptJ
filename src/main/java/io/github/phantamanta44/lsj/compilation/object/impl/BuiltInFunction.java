package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.compilation.object.Type;
import io.github.phantamanta44.lsj.execution.ExecutionContext;
import io.github.phantamanta44.lsj.execution.exception.TypeIE;

import java.util.List;

public abstract class BuiltInFunction<R extends IValue> extends AbstractFunction<BuiltInFunction<R>, R> {

    public BuiltInFunction(String name, Type<R> returnType) {
        super(name, returnType);
    }

    public static abstract class NumeralPredicate extends BuiltInFunction<BooleanValue> {

        public NumeralPredicate(String name) {
            super(name, BuiltIns.T_BOOL);
        }

        public abstract boolean compare(double a, double b);

        @Override
        public BooleanValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
            IValue<?> left = args.get(0).resolve(ctx);
            if (!left.instanceOf(BuiltIns.T_NUMERAL)) throw new TypeIE(BuiltIns.T_NUMERAL, left.getType());
            IValue<?> right = args.get(1).resolve(ctx);
            if (!right.instanceOf(BuiltIns.T_NUMERAL)) throw new TypeIE(BuiltIns.T_NUMERAL, right.getType());
            return new BooleanValue(compare(
                    BuiltIns.T_NUMERAL.cast(left).getAsDouble(), BuiltIns.T_NUMERAL.cast(right).getAsDouble()));
        }

    }

}
