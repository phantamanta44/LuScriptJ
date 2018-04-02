package io.github.phantamanta44.lsj;

import io.github.phantamanta44.lsj.execution.Interpreter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: lsj <source file>");
            System.exit(1);
        } else {
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
                System.err.println(e.getMessage());
            }
        }
    }

}
