package io.github.phantamanta44.lsj.execution;

public class ExecutionContext {

    private final ObjectRegistry objRegistry;
    private int closureDepth;

    public ExecutionContext() {
        this.objRegistry = new ObjectRegistry();
        this.closureDepth = 0;
    }

    public ObjectRegistry getObjRegistry() {
        return objRegistry;
    }

    public int getClosureDepth() {
        return closureDepth;
    }

    public void incrementClosureDepth() {
        closureDepth++;
    }

    public void decrementClosureDepth() {
        closureDepth--;
    }

}
