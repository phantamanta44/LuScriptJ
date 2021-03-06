package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.resyn.parser.token.Token;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;
import io.github.phantamanta44.resyn.parser.token.TokenNode;

public abstract class Node {

    public static Node traverse(Token token) {
        switch (token.getName()) {
            case "closure":
                return NodeClosure.traverse((TokenContainer)token);
            case "function_call":
                return NodeFunctionCall.traverse((TokenContainer)token);
            case "string":
                return NodeLiteralString.traverse((TokenContainer)token);
            case "int":
                return NodeLiteralInt.traverse((TokenNode)token);
            case "float":
                return NodeLiteralFloat.traverse((TokenContainer)token);
            case "list":
                return NodeLiteralList.traverse((TokenContainer)token);
            case "identifier":
                return NodeReference.traverse((TokenNode)token);
        }
        throw new IllegalStateException("Unparsable token: " + token.toString());
    }

    private final int line, pos;

    public Node(Token src) {
        this.line = src.getLine();
        this.pos = src.getPos();
    }

    public int getLine() {
        return line;
    }

    public int getPos() {
        return pos;
    }

}
