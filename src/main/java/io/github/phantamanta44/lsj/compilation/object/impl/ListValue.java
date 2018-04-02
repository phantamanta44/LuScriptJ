package io.github.phantamanta44.lsj.compilation.object.impl;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IExpression;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.ExecutionContext;

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
    private final ListValue tail;

    public ListValue(IExpression<?> head, ListValue tail) {
        super(BuiltIns.T_LIST);
        this.head = head;
        this.tail = tail;
    }

    public IExpression<?> getHead() {
        return head;
    }

    public ListValue getTail() {
        return tail;
    }

    public int size() {
        ListValue list = this;
        int i = 0;
        while (list != null && list.head != null) {
            list = list.tail;
            i++;
        }
        return i;
    }

    public Stream<IExpression<?>> stream() {
        return StreamSupport.stream(Spliterators.spliterator(new ListIterator(this), size(), 0), false);
    }

    @Override
    public String toString() {
        return String.format("[%s]", toTailString());
    }

    private String toTailString() {
        if (tail == null) return head.toString();
        return String.format("%s %s", head, tail.toTailString());
    }

    @Override
    public boolean isEqualTo(ExecutionContext ctx, IValue<?> other) throws InterpretationException {
        if (!other.instanceOf(BuiltIns.T_LIST)) return false;
        ListIterator a = new ListIterator(this);
        ListIterator b = new ListIterator(BuiltIns.T_LIST.cast(other));
        while (a.hasNext() && b.hasNext()) {
            if (!a.next().resolve(ctx).isEqualTo(ctx, b.next().resolve(ctx))) return false;
        }
        return a.hasNext() == b.hasNext();
    }

    private static class ListIterator implements Iterator<IExpression<?>> {

        private ListValue list;

        ListIterator(ListValue list) {
            this.list = list;
        }

        @Override
        public boolean hasNext() {
            return list != null && list.head != null;
        }

        @Override
        public IExpression<?> next() {
            IExpression<?> value = list.head;
            list = list.tail;
            return value;
        }

    }

}
