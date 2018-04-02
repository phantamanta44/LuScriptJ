package io.github.phantamanta44.lsj.compilation.object;

public class Type<T extends IValue> {

    private final String name;
    private final Type<?> parent;

    public Type(String name, Type<?> parent) {
        this.name = name;
        this.parent = parent;
    }

    public Type(String name) {
        this(name, null);
    }

    public String getName() {
        return name;
    }

    public Type getParent() {
        return parent;
    }

    public boolean isOrIsChildOf(Type<?> type) {
        Type<?> checking = this;
        while (checking != null) {
            if (checking == type) return true;
            checking = checking.getParent();
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public T cast(IValue<?> value) {
        return (T)value;
    }

    @Override
    public String toString() {
        return name;
    }

}
