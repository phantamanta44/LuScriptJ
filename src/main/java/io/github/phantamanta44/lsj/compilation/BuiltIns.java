package io.github.phantamanta44.lsj.compilation;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.object.*;
import io.github.phantamanta44.lsj.compilation.object.impl.*;
import io.github.phantamanta44.lsj.execution.ExecutionContext;
import io.github.phantamanta44.lsj.execution.ObjectRegistry;
import io.github.phantamanta44.lsj.execution.exception.ParamCountIE;
import io.github.phantamanta44.lsj.execution.exception.TypeIE;

import java.util.*;
import java.util.stream.Collectors;

public class BuiltIns {

    public static final Type<StringValue> T_STRING = new Type<>("string");
    public static final Type<ListValue> T_LIST = new Type<>("list");
    public static final Type<NumeralValue> T_NUMERAL = new Type<>("numeral");
    public static final Type<IntValue> T_INT = new Type<>("int", T_NUMERAL);
    public static final Type<FloatValue> T_FLOAT = new Type<>("float", T_NUMERAL);
    public static final Type<BooleanValue> T_BOOL = new Type<>("bool");
    public static final Type<EmptyValue> T_EMPTY = new Type<>("empty");
    public static final Type<?> T_ANY = new AnyType();

    @SuppressWarnings("unchecked")
    public static <R extends IValue<R>> Type<R> anyType() {
        return (Type<R>)T_ANY;
    }

    private static final Map<Type<?>, Type<?>> closureTypes = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T extends ICallable<T, R>, R extends IValue<R>> Type<T> closureType(Type<R> returnType) {
        return (Type<T>)closureTypes.computeIfAbsent(returnType,
                t -> new Type<>(String.format("closure<%s>", t.getName())));
    }

    @SuppressWarnings("unchecked")
    public static void registerFunctions(ObjectRegistry registry) {
        registry.put("outj", new BuiltInFunction<EmptyValue>("outj", T_EMPTY) {
            @Override
            public EmptyValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                checkArgCount(args, 2);
                IValue<?> joiner = args.get(0).resolve(ctx);
                if (!joiner.instanceOf(T_STRING)) throw new TypeIE(T_STRING, args.get(0).getType());
                IValue<?> list = args.get(1).resolve(ctx);
                if (!list.instanceOf(T_LIST)) throw new TypeIE(T_LIST, args.get(1).getType());
                System.out.println(T_LIST.cast(list).stream()
                        .map(Object::toString)
                        .collect(Collectors.joining(T_STRING.cast(joiner).toString())));
                return EmptyValue.EMPTY;
            }
        });
        registry.put("outl", new BuiltInFunction<EmptyValue>("outl", T_EMPTY) {
            @Override
            public EmptyValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                checkArgCount(args, 1);
                System.out.println(args.get(0).resolve(ctx).toString());
                return EmptyValue.EMPTY;
            }
        });
        registry.put("else", new BuiltInFunction<IValue>("else", anyType()) {
            @Override
            public IValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                checkArgCount(args, 2);
                IValue<?> value = args.get(1).resolve(ctx);
                if (value.instanceOf(T_EMPTY)) return args.get(0).resolve(ctx);
                return value;
            }
        });
        registry.put("map", new BuiltInFunction<ListValue>("map", T_LIST) {
            @Override
            public ListValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                checkArgCount(args, 2);
                IValue<?> mapperValue = args.get(0).resolve(ctx);
                if (!(mapperValue instanceof ICallable)) throw new TypeIE(closureType(T_ANY), mapperValue.getType());
                ICallable<?, ?> mapper = (ICallable<?, ?>)mapperValue;
                IValue<?> listValue = args.get(1).resolve(ctx);
                if (!listValue.instanceOf(T_LIST)) throw new TypeIE(T_LIST, listValue.getType());
                ListValue list = T_LIST.cast(listValue);
                List<IExpression<?>> elements = new LinkedList<>();
                while (list != null && list.getHead() != null) {
                    elements.add(mapper.invoke(ctx, Collections.singletonList(list.getHead())));
                    list = list.getTail();
                }
                return ListValue.construct(elements);
            }
        });
        registry.put("filt", new BuiltInFunction<ListValue>("filt", T_LIST) {
            @Override
            public ListValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                checkArgCount(args, 2);
                IValue<?> predValue = args.get(0).resolve(ctx);
                if (!(predValue instanceof ICallable)) throw new TypeIE(closureType(T_ANY), predValue.getType());
                ICallable<?, ?> pred = (ICallable<?, ?>)predValue;
                IValue<?> listValue = args.get(1).resolve(ctx);
                if (!listValue.instanceOf(T_LIST)) throw new TypeIE(T_LIST, listValue.getType());
                ListValue list = T_LIST.cast(listValue);
                List<IExpression<?>> elements = new LinkedList<>();
                while (list != null && list.getHead() != null) {
                    IValue<?> result = pred.invoke(ctx, Collections.singletonList(list.getHead()));
                    if (!result.instanceOf(T_BOOL)) throw new TypeIE(T_BOOL, result.getType());
                    if (T_BOOL.cast(result).getValue()) elements.add(list.getHead().resolve(ctx));
                    list = list.getTail();
                }
                return ListValue.construct(elements);
            }
        });
        registry.put("cat", new BuiltInFunction<IValue>("cat", anyType()) {
            @Override
            public IValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                if (args.size() < 2) throw new ParamCountIE(this, "at least 2", args.size());
                StringBuilder accum = new StringBuilder();
                for (IExpression<?> arg : args) {
                    IValue<?> value = arg.resolve(ctx);
                    if (value.instanceOf(T_STRING)) {
                        accum.append(T_STRING.cast(value).toString());
                    } else if (!value.instanceOf(T_EMPTY)) {
                        throw new TypeIE(T_STRING, value.getType());
                    }
                }
                if (accum.length() == 0) return EmptyValue.EMPTY;
                return new StringValue(accum.toString());
            }
        });
        registry.put("if", new BuiltInFunction<IValue>("if", anyType()) {
            @Override
            public IValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                IValue<?> condition = args.get(0).resolve(ctx);
                if (!condition.instanceOf(T_BOOL)) throw new TypeIE(T_BOOL, condition.getType());
                if (T_BOOL.cast(condition).getValue()) return args.get(1).resolve(ctx);
                return EmptyValue.EMPTY;
            }
        });
        registry.put("=", new BuiltInFunction<BooleanValue>("=", T_BOOL) {
            @Override
            public BooleanValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                IValue<?> left = args.get(0).resolve(ctx);
                IValue<?> right = args.get(1).resolve(ctx);
                return new BooleanValue(left.isEqualTo(ctx, right));
            }
        });
        registry.put(">", new BuiltInFunction.NumeralPredicate(">") {
            @Override
            public boolean compare(double a, double b) {
                return a > b;
            }
        });
        registry.put("<", new BuiltInFunction.NumeralPredicate("<") {
            @Override
            public boolean compare(double a, double b) {
                return a < b;
            }
        });
        registry.put(">=", new BuiltInFunction.NumeralPredicate(">=") {
            @Override
            public boolean compare(double a, double b) {
                return a >= b;
            }
        });
        registry.put("<=", new BuiltInFunction.NumeralPredicate("<=") {
            @Override
            public boolean compare(double a, double b) {
                return a <= b;
            }
        });
        registry.put("%", new BuiltInFunction<IntValue>("%", T_INT) {
            @Override
            public IntValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                IValue<?> left = args.get(0).resolve(ctx);
                if (!left.instanceOf(T_INT)) throw new TypeIE(T_INT, left.getType());
                IValue<?> right = args.get(1).resolve(ctx);
                if (!right.instanceOf(T_INT)) throw new TypeIE(T_INT, right.getType());
                return new IntValue(T_INT.cast(left).getValue() % T_INT.cast(right).getValue());
            }
        });
        registry.put("&", new BuiltInFunction<BooleanValue>("&", T_BOOL) {
            @Override
            public BooleanValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                IValue<?> left = args.get(0).resolve(ctx);
                if (!left.instanceOf(T_BOOL)) throw new TypeIE(T_BOOL, left.getType());
                IValue<?> right = args.get(1).resolve(ctx);
                if (!right.instanceOf(T_BOOL)) throw new TypeIE(T_BOOL, right.getType());
                return new BooleanValue(T_BOOL.cast(left).getValue() && T_BOOL.cast(right).getValue());
            }
        });
        registry.put("take", new BuiltInFunction<ListValue>("take", T_LIST) {
            @Override
            public ListValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                IValue<?> left = args.get(0).resolve(ctx);
                if (!left.instanceOf(T_INT)) throw new TypeIE(T_INT, left.getType());
                IValue<?> right = args.get(1).resolve(ctx);
                if (!right.instanceOf(T_LIST)) throw new TypeIE(T_LIST, right.getType());
                return ListValue.construct(T_LIST.cast(right).stream()
                        .limit(T_INT.cast(left).getValue()).collect(Collectors.toList()));
            }
        });
        registry.put("until", new BuiltInFunction<ListValue>("until", T_LIST) {
            @Override
            public ListValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                IValue<?> predValue = args.get(0).resolve(ctx);
                if (!(predValue instanceof ICallable)) throw new TypeIE(closureType(T_ANY), predValue.getType());
                ICallable<?, ?> pred = (ICallable<?, ?>)predValue;
                IValue<?> right = args.get(1).resolve(ctx);
                if (!right.instanceOf(T_LIST)) throw new TypeIE(T_LIST, right.getType());
                ListValue list = T_LIST.cast(right);
                List<IExpression<?>> elements = new LinkedList<>();
                while (list != null && list.getHead() != null) {
                    IValue<?> result = pred.invoke(ctx, Collections.singletonList(list.getHead()));
                    if (!result.instanceOf(T_BOOL)) throw new TypeIE(T_BOOL, result.getType());
                    if (T_BOOL.cast(result).getValue()) break;
                    elements.add(list.getHead().resolve(ctx));
                    list = list.getTail();
                }
                return ListValue.construct(elements);
            }
        });
        registry.put("iter", new BuiltInFunction<ListValue>("iter", T_LIST) {
            @Override
            public ListValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                return null; // TODO Implement
            }
        });
        registry.put("sum", new BuiltInFunction<NumeralValue>("sum", T_NUMERAL) {
            @Override
            public NumeralValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                return null; // TODO Implement
            }
        });
        registry.put("head", new BuiltInFunction<IValue>("head", anyType()) {
            @Override
            public IValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                return null; // TODO Implement
            }
        });
        registry.put("tail", new BuiltInFunction<ListValue>("tail", T_LIST) {
            @Override
            public ListValue invoke(ExecutionContext ctx, List<IExpression<?>> args) throws InterpretationException {
                return null; // TODO Implement
            }
        });
    }

}
