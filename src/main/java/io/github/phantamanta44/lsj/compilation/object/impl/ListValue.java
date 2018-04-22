package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.ICallable;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;
import io.github.phantamanta44.lsj.execution.exception.TypeIE;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ListValue extends AbstractValue<ListValue> {

    public static ListValue construct(List<IExpression<?>> values) {
        if (values.isEmpty()) return new ListValue(null, null);
        ListValue list = new ListValue(values.get(values.size() - 1), null);
        for (int i = values.size() - 2; i >= 0; i--) list = new ListValue(values.get(i), list);
        return list;
    }

    private final IExpression<?> head;
    private final IExpression<ListValue> tail;

    public ListValue(IExpression<?> head, IExpression<ListValue> tail) {
        super(BuiltIns.T_LIST);
        this.head = head;
        this.tail = tail;
    }

    public IExpression<?> getHead() throws InterpretationException {
        return head;
    }

    public IExpression<ListValue> getTail() throws InterpretationException {
        return tail;
    }

    public ListValue map(ExecutionContext ctx, ICallable<?, ?> mapper) {
        return new MappingListValue(ctx, mapper, this);
    }

    public ListValue filter(ExecutionContext ctx, ICallable<?, ?> pred) throws InterpretationException {
        ListValue list = new FilterListValue(ctx, pred, this, false);
        if (list.getHead() == null) {
            IExpression<ListValue> tail = list.getTail();
            return tail == null ? list : tail.resolve(ctx);
        }
        return list;
    }

    public int size(ExecutionContext ctx) {
        Iterator<IExpression<?>> iter = iter(ctx);
        int i = 0;
        while (iter.hasNext()) {
            iter.next();
            i++;
        }
        return i;
    }

    public Stream<IExpression<?>> stream(ExecutionContext ctx) {
        return StreamSupport.stream(Spliterators.spliterator(iter(ctx), size(ctx), 0), false);
    }

    public Iterator<IExpression<?>> iter(ExecutionContext ctx) {
        return new ListIterator(ctx, this);
    }

    @Override
    public String asDisplayString(ExecutionContext ctx) throws InterpretationException {
        return String.format("[%s]", toTailString(ctx));
    }

    private String toTailString(ExecutionContext ctx) throws InterpretationException {
        IExpression<?> tailExpr = getTail();
        if (getHead() == null) return "";
        if (tailExpr == null) return getHead().resolve(ctx).asDisplayString(ctx);
        return tailExpr instanceof ListValue
                ? String.format("%s %s",
                        getHead().resolve(ctx).asDisplayString(ctx), ((ListValue)tailExpr).toTailString(ctx))
                : String.format("%s ...", getHead().resolve(ctx).asDisplayString(ctx));
    }

    @Override
    public boolean isEqualTo(ExecutionContext ctx, IValue<?> other) throws InterpretationException {
        if (!other.instanceOf(BuiltIns.T_LIST)) return false;
        Iterator<IExpression<?>> a = iter(ctx);
        Iterator<IExpression<?>> b = BuiltIns.T_LIST.cast(other).iter(ctx);
        while (a.hasNext() && b.hasNext()) {
            if (!a.next().resolve(ctx).isEqualTo(ctx, b.next().resolve(ctx))) return false;
        }
        return a.hasNext() == b.hasNext();
    }

    private static class ListIterator implements Iterator<IExpression<?>> {

        private final ExecutionContext ctx;
        private ListValue list;

        ListIterator(ExecutionContext ctx, ListValue list) {
            this.ctx = ctx;
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            try {
                return list != null && list.getHead() != null;
            } catch (InterpretationException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public IExpression<?> next() {
            try {
                IExpression<?> value = list.getHead();
                list = list.getTail() != null ? list.getTail().resolve(ctx) : null;
                return value;
            } catch (InterpretationException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private static class MappingListValue extends ListValue {

        private final ExecutionContext ctx;
        private final ICallable<?, ?> mapper;
        private final ListValue backing;

        MappingListValue(ExecutionContext ctx, ICallable<?, ?> mapper, ListValue backing) {
            super(backing.head, backing.tail);
            this.ctx = ctx;
            this.mapper = mapper;
            this.backing = backing;
        }

        @Override
        public IExpression<?> getHead() throws InterpretationException {
            return mapper.invoke(ctx, Collections.singletonList(backing.getHead()));
        }

        @Override
        public IExpression<ListValue> getTail() throws InterpretationException {
            return backing.getTail() != null
                    ? new MappingListValue(ctx, mapper, backing.getTail().resolve(ctx))
                    : null;
        }

    }

    private static class FilterListValue extends ListValue {

        private final ExecutionContext ctx;
        private final ICallable<?, ?> pred;
        private final ListValue backing;
        private final boolean notFirst;

        FilterListValue(ExecutionContext ctx, ICallable<?, ?> pred, ListValue backing, boolean notFirst) {
            super(backing.head, backing.tail);
            this.ctx = ctx;
            this.pred = pred;
            this.backing = backing;
            this.notFirst = notFirst;
        }

        @Override
        public IExpression<?> getHead() throws InterpretationException {
            if (notFirst) return backing.getHead();
            IExpression<?> value = backing.getHead();
            IValue<?> result = pred.invoke(ctx, Collections.singletonList(value));
            if (!result.instanceOf(BuiltIns.T_BOOL)) throw new TypeIE(BuiltIns.T_BOOL, result.getType());
            return BuiltIns.T_BOOL.cast(result).getValue() ? value : null;
        }

        @Override
        public IExpression<ListValue> getTail() throws InterpretationException {
            ListValue list = backing;
            while (list.getTail() != null) {
                list = list.getTail().resolve(ctx);
                IValue<?> result = pred.invoke(ctx, Collections.singletonList(list.getHead()));
                if (!result.instanceOf(BuiltIns.T_BOOL)) throw new TypeIE(BuiltIns.T_BOOL, result.getType());
                if (BuiltIns.T_BOOL.cast(result).getValue()) return new FilterListValue(ctx, pred, list, true);
            }
            return null;
        }

    }

}
