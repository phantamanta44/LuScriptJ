package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.resyn.parser.token.TokenNode;

public class NodeLiteralInt implements INode {

    public static NodeLiteralInt traverse(TokenNode token) {
        return new NodeLiteralInt(Integer.parseInt(token.getContent()));
    }

    public final int value;

    public NodeLiteralInt(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

}
