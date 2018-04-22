package io.github.phantamanta44.lsj.execution;

import io.github.phantamanta44.lsj.InterpretationException;
import io.github.phantamanta44.lsj.compilation.Compiler;
import io.github.phantamanta44.lsj.execution.call.IRootCall;
import io.github.phantamanta44.lsj.tokenization.TokenMarshal;
import io.github.phantamanta44.lsj.tokenization.Tokenizer;
import io.github.phantamanta44.lsj.tokenization.node.INode;
import io.github.phantamanta44.lsj.tokenization.node.NodeFunctionCall;
import io.github.phantamanta44.resyn.parser.ParsingException;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

import java.util.List;

public class Interpreter {

    private final Tokenizer tokenizer;

    public Interpreter() {
        this.tokenizer = Tokenizer.create();
    }

    public List<INode> parse(String src) throws InterpretationException {
        TokenContainer syntaxTree;
        try {
            syntaxTree = tokenizer.tokenize(src);
        } catch (ParsingException e) {
            throw new InterpretationException(e.getMessage(), e);
        }
        return TokenMarshal.traverse(syntaxTree);
    }

    public List<IRootCall> compile(List<INode> rootNodes) throws InterpretationException {
        Compiler compiler = new Compiler();
        for (INode node : rootNodes) {
            if (node instanceof NodeFunctionCall) compiler.acceptFunctionCall((NodeFunctionCall)node);
        }
        return compiler.getOutput();
    }

    public void execute(String src) throws InterpretationException {
        List<IRootCall> calls = compile(parse(src));
        ExecutionContext context = new ExecutionContext();
        try {
            for (IRootCall call : calls) call.performCall(context);
        } catch (RuntimeException e) {
            if (e.getCause() instanceof InterpretationException) throw (InterpretationException)e.getCause();
            throw e;
        }
    }

}
