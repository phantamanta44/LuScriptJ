package io.github.phantamanta44.lsj;

public class InterpretationException extends Exception {

    public InterpretationException(String reason) {
        super(reason);
    }

    public InterpretationException(String reason, Throwable cause) {
        super(reason, cause);
    }

}
