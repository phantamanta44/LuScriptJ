package io.github.phantamanta44.lsj.tokenization;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.tokenization.node.Node;
import io.github.phantamanta44.lsj.tokenization.node.NodeFunctionCall;
import io.github.phantamanta44.resyn.parser.token.Token;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TokenMarshal {

    public static List<Node> traverse(TokenContainer tRoot) throws InterpretationException {
        try {
            return tRoot.getChildren().stream()
                    .map(tRootToken -> {
                        switch (tRootToken.getName()) {
                            case "function_call":
                                return NodeFunctionCall.traverse(c(tRootToken));
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(LinkedList::new));
        } catch (IllegalStateException e) {
            throw new InterpretationException(e.getMessage(), e);
        }
    }

    public static TokenContainer c(Token token) {
        return (TokenContainer)token;
    }

    public static TokenNode n(Token token) {
        return (TokenNode)token;
    }

}
