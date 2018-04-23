package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.lsj.tokenization.model.ICallableNode;
import io.github.phantamanta44.resyn.parser.token.Token;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

public class NodeClosure extends Node implements ICallableNode {

    public static NodeClosure traverse(TokenContainer token) {
        return new NodeClosure(
                token, Node.traverse(((TokenContainer)token.getChildren().get(0)).getChildren().get(0)));
    }

    public final Node expression;

    public NodeClosure(Token src, Node expression) {
        super(src);
        this.expression = expression;
    }

    @Override
    public String toString() {
        return String.format("() -> %s", expression);
    }
}
