package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.lsj.tokenization.model.ICallableNode;
import io.github.phantamanta44.resyn.parser.token.IToken;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

import java.util.List;
import java.util.stream.Collectors;

public class NodeFunctionCall implements INode {

    public static NodeFunctionCall traverse(TokenContainer token) {
        List<IToken> children = token.getChildren();
        IToken tCallable = children.get(0);
        List<INode> params = children.stream().skip(1).map(INode::traverse).collect(Collectors.toList());
        return new NodeFunctionCall(ICallableNode.traverse(((TokenContainer)tCallable).getChildren().get(0)), params);
    }

    public final ICallableNode function;
    public final List<INode> params;

    public NodeFunctionCall(ICallableNode function, List<INode> params) {
        this.function = function;
        this.params = params;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", function.toString(),
                params.stream().map(INode::toString).collect(Collectors.joining(", ")));
    }

}
