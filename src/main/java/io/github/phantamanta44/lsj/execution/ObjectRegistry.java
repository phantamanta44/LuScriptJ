package io.github.phantamanta44.lsj.execution;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.BuiltIns;
import io.github.phantamanta44.lsj.compilation.object.IValue;
import io.github.phantamanta44.lsj.execution.exception.ResolutionIE;

import java.util.HashMap;
import java.util.Map;

public class ObjectRegistry {

    private Map<String, IValue<?>> registry;

    public ObjectRegistry() {
        this.registry = new HashMap<>();
        BuiltIns.registerFunctions(this);
    }

    public void put(String name, IValue<?> value) {
        registry.put(name, value);
    }

    public void clear(String name) {
        registry.remove(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends IValue<T>> T resolve(String name) throws InterpretationException {
        if (registry.containsKey(name)) return (T)registry.get(name);
        throw new ResolutionIE(name);
    }

}
