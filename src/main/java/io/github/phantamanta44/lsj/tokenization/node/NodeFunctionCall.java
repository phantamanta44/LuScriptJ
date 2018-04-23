package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.lsj.tokenization.model.ICallableNode;
import io.github.phantamanta44.resyn.parser.token.Token;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

import java.util.List;
import java.util.stream.Collectors;

public class NodeFunctionCall extends Node {

    public static NodeFunctionCall traverse(TokenContainer token) {
        List<Token> children = token.getChildren();
        Token tCallable = children.get(0);
        List<Node> params = children.stream().skip(1).map(Node::traverse).collect(Collectors.toList());
        return new NodeFunctionCall(
                token, ICallableNode.traverse(((TokenContainer)tCallable).getChildren().get(0)), params);
    }

    public final ICallableNode function;
    public final List<Node> params;

    public NodeFunctionCall(Token src, ICallableNode function, List<Node> params) {
        super(src);
        this.function = function;
        this.params = params;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", function.toString(),
                params.stream().map(Node::toString).collect(Collectors.joining(", ")));
    }

}
