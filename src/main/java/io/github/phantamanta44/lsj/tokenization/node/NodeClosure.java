package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.lsj.tokenization.model.ICallableNode;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

public class NodeClosure implements INode, ICallableNode {

    public static NodeClosure traverse(TokenContainer token) {
        return new NodeClosure(INode.traverse(((TokenContainer)token.getChildren().get(0)).getChildren().get(0)));
    }

    public final INode expression;

    public NodeClosure(INode expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return String.format("() -> %s", expression);
    }
}
