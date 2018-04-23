package io.github.phantamanta44.lsj;

import io.github.phantamanta44.lsj.execution.ExecutionContext;
import io.github.phantamanta44.lsj.execution.Interpreter;
import io.github.phantamanta44.lsj.execution.call.RootCall;
import io.github.phantamanta44.lsj.tokenization.TokenMarshal;
import io.github.phantamanta44.lsj.tokenization.Tokenizer;
import io.github.phantamanta44.lsj.util.Escapes;
import io.github.phantamanta44.resyn.parser.Parser;
import io.github.phantamanta44.resyn.parser.ParsingException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            Parser parser = Tokenizer.get().createParser();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
                Interpreter interpreter = new Interpreter();
                ExecutionContext ctx = new ExecutionContext();
                while (true) {
                    System.out.printf("%s%s",
                            parser.isRootContext()
                                    ? Escapes.CYAN + "lusc: "
                                    : String.format("%s%4d. ", Escapes.BLUE, parser.getLine()),
                            Escapes.RESET);
                    String line = in.readLine();
                    if (line == null) System.exit(0);
                    try {
                        parser.parseLine(line);
                    } catch (ParsingException e) {
                        System.out.println(Escapes.RED + e.getMessage());
                    }
                    if (parser.isRootContext()) {
                        try {
                            for (RootCall call : interpreter.compile(TokenMarshal.traverse(parser.getTree()))) {
                                System.out.printf("   %s-> %s\n", Escapes.MAGENTA,
                                        interpreter.execute(call, ctx).asDisplayString(ctx));
                            }
                        } catch (InterpretationException e) {
                            System.out.println(Escapes.RED + e.getFullMessage() + Escapes.RESET);
                        }
                        parser.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else if (args.length == 1) {
            String src = null;
            try {
                src = Files.readAllLines(new File(args[0]).toPath()).stream()
                        .collect(Collectors.joining("\n"));
            } catch (IOException e) {
                System.err.printf("Failed to read source file: %s\n", e.getMessage());
                System.exit(1);
            }
            try {
                new Interpreter().execute(src);
            } catch (InterpretationException e) {
                System.err.println(e.getFullMessage());
                System.exit(1);
            }
        } else {
            System.out.println("Usage: lsj <source file>");
            System.exit(1);
        }
    }

}
