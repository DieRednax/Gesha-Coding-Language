package com.redfox.gesha.tokenizer;

import com.redfox.gesha.tokenizer.statics.TokenStatics;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String src;

    private int line = 1;

    private int start = 0;
    private int current = 0;

    public Lexer(final String src) {
        this.src = src;
    }

    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();

        while (!isAtEnd()) {
            start = current;
            scanTokens(tokens);
        }

        tokens.add(new Token(TokenType.END, "", null, line));
        return tokens;
    }

    private void scanTokens(List<Token> tokens) {
        char c = advance();

        switch (c) {
            case '+' -> addToken(tokens, TokenType.OPERATOR_ADD, null);
            case '-' -> addToken(tokens, TokenType.OPERATOR_SUB, null);
            case '*' -> addToken(tokens, TokenType.OPERATOR_MUL, null);
            case '/' -> addToken(tokens, TokenType.OPERATOR_DIV, null);
            case '^' -> addToken(tokens, TokenType.OPERATOR_POW, null);
            case '\\' -> addToken(tokens, TokenType.OPERATOR_RT, null);
            case ';' -> addToken(tokens, TokenType.SEMI_COLON, null);

            case '=' -> addToken(
                    tokens,
                    match('=') ? TokenType.OPERATOR_EQ
                            : TokenType.ASSIGN_EQ,
                    null
            );

            case ' ', '\r', '\t' -> {}

            case '\n' -> line++;

            default -> {
                if (Character.isDigit(c)) {
                    scanNumber(tokens);
                } else if (Character.isLetter(c) || c == '_') {
                    scanIdentifier(tokens);
                } else {
                    throw new RuntimeException(
                            "Unexpected character '" + c + "' at line " + line
                    );
                }
            }
        }
    }

    private void addToken(List<Token> tokens, TokenType type, String literal) {
        tokens.add(new Token(type, currentLexeme(), literal, line));
    }
    private void addToken(List<Token> tokens, TokenType type, String literal, String lexeme) {
        tokens.add(new Token(type, lexeme, literal, line));
    }

    private void scanIdentifier(List<Token> tokens) {
        while (Character.isLetterOrDigit(peek())
                || peek() == '_') {
            advance();
        }

        String lexeme = currentLexeme();

        if (TokenStatics.getKeywords().contains(lexeme)) {
            addToken(tokens, TokenType.KEYWORD, null, lexeme);
        } else {
            addToken(tokens, TokenType.IDENTIFIER, null, lexeme);
        }
    }

    private void scanNumber(List<Token> tokens) {
        while (Character.isDigit(peek())) {
            advance();
        }

        String lexeme = currentLexeme();

        try {
            Integer value = Integer.parseInt(lexeme);
            addToken(tokens, TokenType.INT_LITERAL,
                    value.toString());
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Invalid integer literal: " + lexeme
                            + " at line " + line,
                    e
            );
        }
    }

    //factories
    private boolean isAtEnd() {
        return current >= src.length();
    }

    //consumes 1 char
    private char advance() {
        return src.charAt(current++);
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }

        return src.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= src.length()) {
            return '\0';
        }

        return src.charAt(current + 1);
    }

    //consume 1 char at match
    private boolean match(char expected) {
        if (isAtEnd() || src.charAt(current) != expected) {
            return false;
        }

        current++;
        return true;
    }

    private String currentLexeme() {
        return src.substring(start, current);
    }
}
