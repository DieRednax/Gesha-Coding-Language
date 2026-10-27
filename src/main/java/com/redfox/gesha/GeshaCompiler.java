package com.redfox.gesha;

import com.redfox.gesha.tokenizer.Lexer;
import com.redfox.gesha.tokenizer.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class GeshaCompiler {
    public static void main(String[] args) {
        if (2 > args.length) {
            System.err.println("At least 2 input params must be supplied.");
            return;
        }

        System.out.println("Param 0: " + args[0]);
        System.out.println("Param 1: " + args[1]);

        System.out.println("Reading src code...");
        String input;
        try {
            input = readInputFile(Paths.get(args[0]));
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return;
        }

        System.out.println("Lexing...");
        List<Token> tokens = new Lexer(input).tokenize();
        System.out.println("Tokens: ");
        for (Token token : tokens) {
            System.out.println(token.getTokenType() + ": " + token.getLexeme());
        }
    }

    private static String readInputFile(Path filePath) throws IOException {
        return Files.readString(filePath);
    }
}
