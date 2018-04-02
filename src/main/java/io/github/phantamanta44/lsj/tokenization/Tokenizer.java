package io.github.phantamanta44.lsj.tokenization;

import io.github.phantamanta44.resyn.parser.Parser;
import io.github.phantamanta44.resyn.parser.ParsingException;
import io.github.phantamanta44.resyn.parser.token.TokenContainer;

import java.io.BufferedInputStream;
import java.io.IOException;

public class Tokenizer {

    public static Tokenizer create() {
        try (BufferedInputStream in = new BufferedInputStream(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("luscript.rsn"))) {
            StringBuilder sb = new StringBuilder();
            int character;
            while ((character = in.read()) != -1)
                sb.append((char)character);
            return new Tokenizer(Parser.create(sb.toString()));
        } catch (ParsingException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private final Parser parser;

    private Tokenizer(Parser parser) {
        this.parser = parser;
    }

    public TokenContainer tokenize(String src) throws ParsingException {
        return parser.parse(src);
    }

}
