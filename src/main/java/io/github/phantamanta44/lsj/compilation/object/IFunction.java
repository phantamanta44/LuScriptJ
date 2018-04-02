package io.github.phantamanta44.lsj.compilation.object;

public interface IFunction<T extends IFunction<T, R>, R extends IValue> extends ICallable<T, R> {

    String getName();

}
