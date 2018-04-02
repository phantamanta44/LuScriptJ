package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.resyn.parser.token.TokenContainer;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

public class NodeLiteralFloat implements INode {

    public static NodeLiteralFloat traverse(TokenContainer token) {
        if (token.getChildren().get(1) != null) {
            return new NodeLiteralFloat(Float.parseFloat(
                    ((TokenNode)token.getChildren().get(0)).getContent() + "."
                            + ((TokenNode)token.getChildren().get(1)).getContent()));
        } else {
            return new NodeLiteralFloat(Float.parseFloat(((TokenNode)token.getChildren().get(0)).getContent()));
        }
    }

    public final double value;

    public NodeLiteralFloat(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Double.toString(value);
    }

}
