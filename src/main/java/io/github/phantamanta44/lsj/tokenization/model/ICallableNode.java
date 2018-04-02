package io.github.phantamanta44.lsj.tokenization.model;

import io.github.phantamanta44.lsj.tokenization.node.NodeClosure;
import io.github.phantamanta44.lsj.tokenization.node.NodeReference;
import io.github.phantamanta44.resyn.parser.token.IToken;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

public interface ICallableNode {

    static ICallableNode traverse(IToken token) {
        switch (token.getName()) {
            case "closure":
                return NodeClosure.traverse((TokenContainer)token);
            case "identifier":
                return NodeReference.traverse((TokenNode)token);
        }
        throw new IllegalStateException("Unparsable token: " + token.toString());
    }

}
