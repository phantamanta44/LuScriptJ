package io.github.phantamanta44.lsj;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class InterpretationException extends Exception {

    private int line, pos;
    private List<StackFrame> stack;

    public InterpretationException(String reason) {
        super(reason);
        this.line = this.pos = -1;
        this.stack = new LinkedList<>();
    }

    public InterpretationException(String reason, Throwable cause) {
        super(reason, cause);
        stack = new LinkedList<>();
    }

    public InterpretationException from(int line, int pos) {
        this.line = line;
        this.pos = pos;
        return this;
    }

    public InterpretationException withFrame(String call, int line, int pos) {
        stack.add(new StackFrame(call != null ? call : "<anon>", line, pos));
        return this;
    }

    @Override
    public String getMessage() {
        return line > 0 && pos > 0 ? String.format("%s (%d:%d)", super.getMessage(), line, pos) : super.getMessage();
    }

    public String getFullMessage() {
        return stack.isEmpty() ? getMessage() : String.format("%s\n%s",
                getMessage(), stack.stream().map(Object::toString).collect(Collectors.joining("\n")));
    }

    private static class StackFrame {

        private final String call;
        private final int line;
        private final int pos;

        public StackFrame(String call, int line, int pos) {
            this.call = call;
            this.line = line;
            this.pos = pos;
        }

        @Override
        public String toString() {
            return String.format(" at %s (%d:%d)", call, line, pos);
        }

    }

}
