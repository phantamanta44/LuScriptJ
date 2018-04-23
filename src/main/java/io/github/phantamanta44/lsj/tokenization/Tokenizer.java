package io.github.phantamanta44.lsj.tokenization;

import io.github.phantamanta44.resyn.parser.Parser;
import io.github.phantamanta44.resyn.parser.ParsingException;
import io.github.phantamanta44.resyn.parser.Syntax;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

import java.io.BufferedInputStream;
import java.io.IOException;

public class Tokenizer {

    private static Tokenizer INSTANCE = null;

    public static Tokenizer get() {
        if (INSTANCE == null) {
            try (BufferedInputStream in = new BufferedInputStream(
                    Thread.currentThread().getContextClassLoader().getResourceAsStream("luscript.rsn"))) {
                StringBuilder sb = new StringBuilder();
                int character;
                while ((character = in.read()) != -1)
                    sb.append((char)character);
                INSTANCE = new Tokenizer(Syntax.create(sb.toString()));
            } catch (ParsingException | IOException e) {
                throw new IllegalStateException(e);
            }
        }
        return INSTANCE;
    }

    private final Syntax syntax;

    private Tokenizer(Syntax syntax) {
        this.syntax = syntax;
    }

    public Parser createParser() {
        return syntax.newPartialParser();
    }

    public TokenContainer tokenize(String src) throws ParsingException {
        return syntax.parse(src);
    }

}
