package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.lsj.tokenization.model.ICallableNode;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

public class NodeReference implements INode, ICallableNode {

    public static NodeReference traverse(TokenNode token) {
        return new NodeReference(token.getContent());
    }

    public final String identifier;

    public NodeReference(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }

}
