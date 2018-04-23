package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.resyn.parser.token.Token;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

import java.util.List;
import java.util.stream.Collectors;

public class NodeLiteralList extends Node {

    public static NodeLiteralList traverse(TokenContainer token) {
        List<Token> children = token.getChildren();
        if (children.get(children.size() - 1).getName().equals("list_middle")) {
            if (children.size() != 2) throw new IllegalStateException("Invalid list range start");
            List<Token> subChildren = ((TokenContainer)children.get(1)).getChildren();
            if (subChildren.get(subChildren.size() - 1).getName().equals("list_end")) {
                if (subChildren.size() != 2) throw new IllegalStateException("Invalid list range end");
                List<Token> subSubChildren = ((TokenContainer)subChildren.get(1)).getChildren();
                if (subSubChildren.size() != 1) throw new IllegalStateException("Invalid list range step");
                return new Range(token, Node.traverse(children.get(0)),
                        Node.traverse(subChildren.get(0)),
                        Node.traverse(subSubChildren.get(0)));
            }
            if (subChildren.size() != 1) throw new IllegalStateException("Invalid list range end");
            return new Range(token, Node.traverse(children.get(0)), Node.traverse(subChildren.get(0)), null);
        } else {
            return new Literal(token, children.stream().map(Node::traverse).collect(Collectors.toList()));
        }
    }

    private NodeLiteralList(Token src) {
        super(src);
    }

    public static class Literal extends NodeLiteralList {

        public final List<Node> elements;

        public Literal(Token src, List<Node> elements) {
            super(src);
            this.elements = elements;
        }

        @Override
        public String toString() {
            return String.format("[%s]", elements.stream().map(Node::toString).collect(Collectors.joining(" ")));
        }

    }

    public static class Range extends NodeLiteralList {

        public final Node start;
        public final Node end;
        public final Node step;

        public Range(Token src, Node start, Node end, Node step) {
            super(src);
            this.start = start;
            this.end = end;
            this.step = step;
        }

        @Override
        public String toString() {
            if (step == null) return String.format("[%s:%s]", start, end);
            return String.format("[%s:%s:%s]", start, end, step);
        }

    }

}
