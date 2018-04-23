package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.lsj.tokenization.model.ICallableNode;
import io.github.phantamanta44.resyn.parser.token.Token;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

public class NodeReference extends Node implements ICallableNode {

    public static NodeReference traverse(TokenNode token) {
        return new NodeReference(token, token.getContent());
    }

    public final String identifier;

    public NodeReference(Token src, String identifier) {
        super(src);
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier;
    }

}
