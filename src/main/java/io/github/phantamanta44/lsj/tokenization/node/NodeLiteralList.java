package io.github.phantamanta44.lsj.tokenization.node;

import io.github.phantamanta44.resyn.parser.token.IToken;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

import java.util.List;
import java.util.stream.Collectors;

public class NodeLiteralList implements INode {

    public static NodeLiteralList traverse(TokenContainer token) {
        List<IToken> children = token.getChildren();
        if (children.get(children.size() - 1).getName().equals("list_middle")) {
            if (children.size() != 2) throw new IllegalStateException("Invalid list range start");
            List<IToken> subChildren = ((TokenContainer)children.get(1)).getChildren();
            if (subChildren.get(subChildren.size() - 1).getName().equals("list_end")) {
                if (subChildren.size() != 2) throw new IllegalStateException("Invalid list range end");
                List<IToken> subSubChildren = ((TokenContainer)subChildren.get(1)).getChildren();
                if (subSubChildren.size() != 1) throw new IllegalStateException("Invalid list range step");
                return new Range(INode.traverse(children.get(0)),
                        INode.traverse(subChildren.get(0)),
                        INode.traverse(subSubChildren.get(0)));
            }
            if (subChildren.size() != 1) throw new IllegalStateException("Invalid list range end");
            return new Range(INode.traverse(children.get(0)), INode.traverse(subChildren.get(0)), null);
        } else {
            return new Literal(children.stream().map(INode::traverse).collect(Collectors.toList()));
        }
    }

    public static class Literal extends NodeLiteralList {

        public final List<INode> elements;

        public Literal(List<INode> elements) {
            this.elements = elements;
        }

        @Override
        public String toString() {
            return String.format("[%s]", elements.stream().map(INode::toString).collect(Collectors.joining(" ")));
        }

    }

    public static class Range extends NodeLiteralList {

        public final INode start;
        public final INode end;
        public final INode step;

        public Range(INode start, INode end, INode step) {
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
