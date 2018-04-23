package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.resyn.parser.token.Token;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

import java.util.stream.Collectors;

public class NodeLiteralString extends Node {

    public static NodeLiteralString traverse(TokenContainer token) {
        return new NodeLiteralString(token, token.getChildren().stream()
                .map(t -> ((TokenNode)t).getContent())
                .collect(Collectors.joining()));
    }

    public final String value;

    public NodeLiteralString(Token src, String value) {
        super(src);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", value);
    }

}
