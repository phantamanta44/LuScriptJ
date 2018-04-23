package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.resyn.parser.token.Token;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

public class NodeLiteralInt extends Node {

    public static NodeLiteralInt traverse(TokenNode token) {
        return new NodeLiteralInt(token, Integer.parseInt(token.getContent()));
    }

    public final int value;

    public NodeLiteralInt(Token src, int value) {
        super(src);
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }

}
