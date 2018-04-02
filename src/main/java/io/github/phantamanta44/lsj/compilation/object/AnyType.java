package io.github.phantamanta44.lsj.compilation.object;

public class AnyType extends Type<IValue<?>> {

    public AnyType() {
        super("any");
    }

    @Override
    public boolean isOrIsChildOf(Type<?> type) {
        return true;
    }

    @Override
    public IValue<?> cast(IValue<?> value) {
        return value;
    }

}
