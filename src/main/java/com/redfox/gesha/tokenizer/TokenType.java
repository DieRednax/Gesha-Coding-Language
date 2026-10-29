package com.redfox.gesha.tokenizer;

public enum TokenType {
    NUM_LITERAL,
    IDENTIFIER,
    KEYWORD,
    ASSIGN_EQ,
    DOT_OPERATOR,

    LEFT_P, // (
    RIGHT_P, // )
    LEFT_BP, // [
    RIGHT_BP, // ]
    LEFT_CP, // {
    RIGHT_CP, // }
    LEFT_AP, // <
    RIGHT_AP, // >

    OPERATOR_ADD,
    OPERATOR_SUB,
    OPERATOR_MUL,
    OPERATOR_DIV,
    OPERATOR_MOD,
    OPERATOR_POW,
    OPERATOR_RT,

    OPERATOR_EQ,
//    OPERATOR_NEQ,
//    OPERATOR_GT,
//    OPERATOR_LT,
//    OPERATOR_GE,

    SEMI_COLON,

    END

}
