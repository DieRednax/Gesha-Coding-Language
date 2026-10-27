package com.redfox.gesha.tokenizer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Token {
    private TokenType tokenType;

    private String lexeme;

    private String literal;

    private int line;
}
