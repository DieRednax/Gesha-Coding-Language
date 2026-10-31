package com.redfox.gesha;

import com.redfox.gesha.precompiler.PrecompileOutputData;
import com.redfox.gesha.precompiler.Precompiler;
import com.redfox.gesha.tokenizer.Lexer;
import com.redfox.gesha.tokenizer.Token;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class GeshaCompiler {
    public static void main(String[] args) {
        if (2 > args.length) {
            System.err.println("At least 2 input params must be supplied. Instead present: ");
            System.err.println(Arrays.toString(args));
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

        System.out.println("Precompiling...");
        PrecompileOutputData precompiledSrc = new Precompiler(input).precompile();

        System.out.println("Lexing...");
        List<Token> tokens = new Lexer(precompiledSrc.getOutputSrc(), precompiledSrc.getLineOffset()).tokenize();
//        System.out.println("Tokens: ");
//        for (Token token : tokens) {
//            System.out.println(token.getTokenType() + ": " + token.getLexeme());
//        }

        StringBuilder tokensSB = new StringBuilder();
        int i = 1;
        for (Token token : tokens) {
            if (token.getLine() > i) {
                tokensSB.append("\n");
                i = token.getLine();
            }
            tokensSB
                    .append(token.getTokenType())
                    .append("(\"")
                    .append(token.getLexeme())
                    .append("\", ")
                    .append(token.getLiteral())
                    .append(", ")
                    .append(token.getLine())
                    .append(")");
            tokensSB.append(", ");

        }

        try {
            Path outPathTokens = Path.of(args[1] + "/tokens.rft");
            Path outPathPrecompiledSrc = Path.of(args[1] + "/precompiled.gesha");

            createOrWriteFile(outPathPrecompiledSrc, precompiledSrc.getOutputSrc());
            createOrWriteFile(outPathTokens, tokensSB.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String readInputFile(Path filePath) throws IOException {
        return Files.readString(filePath);
    }
    private static void createOrWriteFile(Path filePath, String content) throws IOException {
        Path parentDir = filePath.getParent();
        if (parentDir != null) Files.createDirectories(parentDir);
        if (Files.notExists(filePath)) Files.createFile(filePath);

        Files.writeString(filePath, content);
    }
}
