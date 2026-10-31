package com.redfox.gesha.tokenizer;

import com.redfox.gesha.tokenizer.statics.TokenStatics;

import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String src;

    private int line = 1;

    private int start = 0;
    private int current = 0;

    public Lexer(final String src, final int lineOffset) {
        this.src = src;
        this.line += lineOffset;
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
            case '^' -> addToken(tokens, TokenType.OPERATOR_POW, null);
            case '\\' -> addToken(tokens, TokenType.OPERATOR_RT, null);
            case ';' -> addToken(tokens, TokenType.SEMI_COLON, null);
            case '.' -> addToken(tokens, TokenType.DOT_OPERATOR, null);
            case '(' -> addToken(tokens, TokenType.LEFT_P, null);
            case ')' -> addToken(tokens, TokenType.RIGHT_P, null);
            case '[' -> addToken(tokens, TokenType.LEFT_BP, null);
            case ']' -> addToken(tokens, TokenType.RIGHT_BP, null);
            case '{' -> addToken(tokens, TokenType.LEFT_CP, null);
            case '}' -> addToken(tokens, TokenType.RIGHT_CP, null);
            case '<' -> addToken(tokens, TokenType.LEFT_AP, null);
            case '>' -> addToken(tokens, TokenType.RIGHT_AP, null);

            case '=' -> addToken(
                    tokens,
                    /*match('=') ? TokenType.OPERATOR_EQ
                            : */TokenType.ASSIGN_EQ,
                    null
            );

            case '/' -> {
                if (!match('/')) {
                    addToken(
                            tokens,
                            match('=') ? TokenType.ASSIGN_DIV
                                    : TokenType.OPERATOR_DIV,
                            null
                    );
                } else removeComment();
            }

            case '+' -> {
                if (match('=')) {
                    addToken(tokens, TokenType.ASSIGN_ADD, null);
                } else if (match('+')) {
                    addToken(tokens, TokenType.OPERATOR_INC, null);
                } else {
                    addToken(tokens, TokenType.OPERATOR_ADD, null);
                }
            }
            case '-' -> {
                if (match('=')) {
                    addToken(tokens, TokenType.ASSIGN_SUB, null);
                } else if (match('-')) {
                    addToken(tokens, TokenType.OPERATOR_DEC, null);
                } else {
                    addToken(tokens, TokenType.OPERATOR_SUB, null);
                }
            }
            case '*' -> addToken(
                    tokens,
                    match('=') ? TokenType.ASSIGN_MUL
                            : TokenType.OPERATOR_MUL,
                    null
            );
            case '%' -> addToken(tokens, TokenType.OPERATOR_MOD, null);

            case ' ', '\r', '\t', ',' -> {}

            case '\n' -> line++;

            default -> {
                if (Character.isDigit(c) || c == '#') {
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

    private void removeComment() {
        while (peek() != '\n') {
            advance();
        }
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
        while (Character.isDigit(peek())
                || (peek() == 'b' && currentLexeme().equals("0"))
                || (peek() == 'x' && currentLexeme().equals("0"))
                || currentLexeme().equals("#")
                || (TokenStatics.getHexCharacters().contains(peek())
                    && (currentLexeme().contains("0x") || currentLexeme().contains("#")))
                || (peek() == '.' && TokenStatics.isNumeric(currentLexeme()))) {
            advance();
        }

        String lexeme = currentLexeme().replace("#", "0x");

        try {
            if (lexeme.contains("0b") || lexeme.contains("0x")) {
                addToken(tokens, TokenType.NUM_LITERAL,
                        Integer.toString(TokenStatics.parseAnyStringToInteger(lexeme)), lexeme);
            } else {
                Double value = Double.parseDouble(lexeme);
                addToken(tokens, TokenType.NUM_LITERAL,
                        value.toString(), lexeme);
            }
        } catch (NumberFormatException e) {
            throw new RuntimeException(
                    "Invalid number literal: " + lexeme
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
