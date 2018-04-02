package io.github.phantamanta44.lsj.execution.exception;

import io.github.phantamanta44.lsj.InterpretationException;

public class ResolutionIE extends InterpretationException {

    public ResolutionIE(String name) {
        super(String.format("Resolution error: could not resolve %s", name));
    }

}
