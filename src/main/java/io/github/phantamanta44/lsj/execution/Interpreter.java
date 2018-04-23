package io.github.phantamanta44.lsj.execution;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.Compiler;
import io.github.phantamanta44.lsj.execution.call.RootCall;
import io.github.phantamanta44.lsj.tokenization.TokenMarshal;
import io.github.phantamanta44.lsj.tokenization.Tokenizer;
import io.github.phantamanta44.lsj.tokenization.node.Node;
import io.github.phantamanta44.lsj.tokenization.node.NodeFunctionCall;
import io.github.phantamanta44.resyn.parser.ParsingException;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

import java.util.List;

public class Interpreter {

    private final Tokenizer tokenizer;

    public Interpreter() {
        this.tokenizer = Tokenizer.create();
    }

    public List<Node> parse(String src) throws InterpretationException {
        TokenContainer syntaxTree;
        try {
            syntaxTree = tokenizer.tokenize(src);
        } catch (ParsingException e) {
            throw new InterpretationException(e.getMessage(), e);
        }
        return TokenMarshal.traverse(syntaxTree);
    }

    public List<RootCall> compile(List<Node> rootNodes) throws InterpretationException {
        Compiler compiler = new Compiler();
        for (Node node : rootNodes) {
            if (node instanceof NodeFunctionCall) compiler.acceptFunctionCall((NodeFunctionCall)node);
        }
        return compiler.getOutput();
    }

    public void execute(String src) throws InterpretationException {
        List<RootCall> calls = compile(parse(src));
        ExecutionContext context = new ExecutionContext();
        for (RootCall call : calls) {
            try {
                call.performCall(context);
            } catch (InterpretationException e) {
                throw e.withFrame("<root>", call.getLine(), call.getPos());
            } catch (RuntimeException e) {
                if (e.getCause() instanceof InterpretationException) {
                    throw ((InterpretationException)e.getCause()).withFrame("<root>", call.getLine(), call.getPos());
                }
                throw e;
            }
        }
    }

}
