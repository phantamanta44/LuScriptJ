package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.resyn.parser.token.TokenContainer;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

import java.util.stream.Collectors;

public class NodeLiteralString implements INode {

    public static NodeLiteralString traverse(TokenContainer token) {
        return new NodeLiteralString(token.getChildren().stream()
                .map(t -> ((TokenNode)t).getContent())
                .collect(Collectors.joining()));
    }

    public final String value;

    public NodeLiteralString(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("\"%s\"", value);
    }

}
