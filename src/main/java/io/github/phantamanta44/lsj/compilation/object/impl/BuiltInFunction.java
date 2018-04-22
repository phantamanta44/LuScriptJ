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

    @Override
    public String asDisplayString(ExecutionContext ctx) {
        return getType().getName();
    }

    public static abstract class NumeralPredicate extends BuiltInFunction<BooleanValue> {

        public NumeralPredicate(String name) {
            super(name, BuiltIns.T_BOOL);
        }

        public abstract boolean compare(double a, double b);

        @Override
        public BooleanValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
            checkArgCount(args, 2);
            IValue<?> left = args.get(0).resolve(ctx);
            if (!left.instanceOf(BuiltIns.T_NUMERAL)) throw new TypeIE(BuiltIns.T_NUMERAL, left.getType());
            IValue<?> right = args.get(1).resolve(ctx);
            if (!right.instanceOf(BuiltIns.T_NUMERAL)) throw new TypeIE(BuiltIns.T_NUMERAL, right.getType());
            return new BooleanValue(compare(
                    BuiltIns.T_NUMERAL.cast(left).asDouble(), BuiltIns.T_NUMERAL.cast(right).asDouble()));
        }

    }

    public static abstract class NumeralOperator extends BuiltInFunction<NumeralValue> {

        public NumeralOperator(String name) {
            super(name, BuiltIns.T_NUMERAL);
        }

        public abstract int applyI(int a, int b);

        public abstract double applyF(double a, double b);

        @Override
        public NumeralValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
            checkArgCount(args, 2);
            IValue<?> left = args.get(0).resolve(ctx);
            if (!left.instanceOf(BuiltIns.T_NUMERAL)) throw new TypeIE(BuiltIns.T_NUMERAL, left.getType());
            IValue<?> right = args.get(1).resolve(ctx);
            if (!right.instanceOf(BuiltIns.T_NUMERAL)) throw new TypeIE(BuiltIns.T_NUMERAL, right.getType());
            return left.instanceOf(BuiltIns.T_INT) && right.instanceOf(BuiltIns.T_INT)
                    ? new IntValue(applyI(
                            BuiltIns.T_INT.cast(left).getValue(), BuiltIns.T_INT.cast(right).getValue()))
                    : new FloatValue(applyF(
                            BuiltIns.T_NUMERAL.cast(left).asDouble(), BuiltIns.T_NUMERAL.cast(right).asDouble()));
        }

    }

    public static abstract class BooleanOperator extends BuiltInFunction<BooleanValue> {

        public BooleanOperator(String name) {
            super(name, BuiltIns.T_BOOL);
        }

        public abstract boolean apply(boolean a, boolean b);

        @Override
        public BooleanValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
            checkArgCount(args, 2);
            IValue<?> left = args.get(0).resolve(ctx);
            if (!left.instanceOf(BuiltIns.T_BOOL)) throw new TypeIE(BuiltIns.T_BOOL, left.getType());
            IValue<?> right = args.get(1).resolve(ctx);
            if (!right.instanceOf(BuiltIns.T_BOOL)) throw new TypeIE(BuiltIns.T_BOOL, right.getType());
            return new BooleanValue(
                    apply(BuiltIns.T_BOOL.cast(left).getValue(), BuiltIns.T_BOOL.cast(right).getValue()));
        }

    }

}
