#ifndef LEXER_H
#define LEXER_H

#include <stdio.h>

// Token definitions from the old project we made.
typedef enum {
    TOKEN_KEYWORD,
    TOKEN_IDENTIFIER,
    TOKEN_INT_CONSTANT,
    TOKEN_STRING_CONSTANT,
    TOKEN_OPERATOR,
    TOKEN_END_OF_LINE,
    TOKEN_OPEN_BLOCK,
    TOKEN_CLOSE_BLOCK,
    TOKEN_ERROR,
    TOKEN_EOF
} TokenType;

typedef struct {
    TokenType type;
    char lexeme[1000];
    int line_number;
} Token;

// Lexer function prototypes
int isKeyword(const char* str);
int isValidNumber(const char* str);
int isValidIdentifier(const char* str);
Token getNextToken(FILE* file);

#endif 