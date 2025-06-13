#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <locale.h>
#include "lexer.h"

// Lexer functions (from old projwct code)
const char* keywords[] = {
    "number", "repeat", "times", "write", "and", "newline"
};
const int num_keywords = 6;

int isKeyword(const char* str) {
    for (int i = 0; i < num_keywords; i++) {
        if (strcmp(str, keywords[i]) == 0) {
            return 1;
        }
    }
    return 0;
}

int isValidNumber(const char* str) {
    int len = strlen(str);
    int i = 0;
    if (str[0] == '-') {
        if (len == 1) return 0;
        i = 1;
    }
    if (str[i] == '0' && len > i + 1) return 0;
    for (; i < len; i++) {
        if (!isdigit(str[i])) return 0;
    }
    return 1;
}

int isValidIdentifier(const char* str) {
    int len = strlen(str);
    if (len > 20) return 0;
    if (!isalpha(str[0])) return 0;
    for (int i = 1; i < len; i++) {
        if (!isalnum(str[i]) && str[i] != '_') return 0;
    }
    return 1;
}

Token getNextToken(FILE* file) {
    static int current_line = 1;
    Token token;
    token.line_number = current_line;
    int c;
    int i = 0;
    
    while ((c = fgetc(file)) != EOF) {
        if (c == '\n') {
            current_line++;
            continue;
        }
        if (isspace(c)) {
            continue;
        }
        if (c == '*') {
            int comment_start_line = current_line;
            while ((c = fgetc(file)) != EOF && c != '*') {
                if (c == '\n') current_line++;
                if (c < 0 || c > 127) {
                }
            }
            if (c == EOF) {
                token.type = TOKEN_ERROR;
                token.line_number = comment_start_line;
                strcpy(token.lexeme, "Unterminated comment");
                return token;
            }
            continue;
        }
        break;
    }

    if (c == EOF) {
        token.type = TOKEN_EOF;
        return token;
    }

    if (c < 0 || c > 127) {
        token.type = TOKEN_ERROR;
        sprintf(token.lexeme, "Invalid character (ASCII code: %d)", c);
        return token;
    }

    if (isalpha(c)) {
        token.lexeme[i++] = c;
        while ((c = fgetc(file)) != EOF && (isalnum(c) || c == '_') && i < 20) {
            if (c < 0 || c > 127) {
                continue;
            }
            token.lexeme[i++] = c;
        }
        token.lexeme[i] = '\0';
        ungetc(c, file);

        if (!isValidIdentifier(token.lexeme)) {
            token.type = TOKEN_ERROR;
            strcpy(token.lexeme, "Invalid identifier format");
            return token;
        }

        if (isKeyword(token.lexeme)) {
            token.type = TOKEN_KEYWORD;
        } else {
            token.type = TOKEN_IDENTIFIER;
        }
        return token;
    }
    
    if (isdigit(c)) {
        token.lexeme[i++] = c;
        while ((c = fgetc(file)) != EOF && isdigit(c)) {
            token.lexeme[i++] = c;
        }
        token.lexeme[i] = '\0';
        ungetc(c, file);
        
        if (!isValidNumber(token.lexeme)) {
            token.type = TOKEN_ERROR;
            strcpy(token.lexeme, "Invalid number format");
            return token;
        }
        
        token.type = TOKEN_INT_CONSTANT;
        return token;
    }
    
    if (c == '"') {
        int string_start_line = current_line;
        while ((c = fgetc(file)) != EOF && c != '"') {
            if (c == '\n') current_line++;
            if (c < 0 || c > 127) {
                continue;
            }
            token.lexeme[i++] = c;
        }
        if (c == EOF) {
            token.type = TOKEN_ERROR;
            token.line_number = string_start_line;
            strcpy(token.lexeme, "Unterminated string");
            return token;
        }
        token.lexeme[i] = '\0';
        token.type = TOKEN_STRING_CONSTANT;
        return token;
    }
    
    switch (c) {
        case ':':
            token.lexeme[i++] = c;
            c = fgetc(file);
            if (c == '=') {
                token.lexeme[i++] = c;
                token.lexeme[i] = '\0';
                token.type = TOKEN_OPERATOR;
            } else {
                ungetc(c, file);
                token.lexeme[i] = '\0';
                token.type = TOKEN_ERROR;
                strcpy(token.lexeme, "Invalid operator");
            }
            break;
        case '+':
            token.lexeme[i++] = c;
            c = fgetc(file);
            if (c == '=') {
                token.lexeme[i++] = c;
                token.lexeme[i] = '\0';
                token.type = TOKEN_OPERATOR;
            } else {
                ungetc(c, file);
                token.lexeme[i] = '\0';
                token.type = TOKEN_OPERATOR;
            }
            break;
        case '-':
            token.lexeme[i++] = c;
            c = fgetc(file);
            if (c == '=') {
                token.lexeme[i++] = c;
                token.lexeme[i] = '\0';
                token.type = TOKEN_OPERATOR;
            } else if (isdigit(c)) {
                // Handle negative numbers
                token.lexeme[i++] = c;
                while ((c = fgetc(file)) != EOF && isdigit(c)) {
                    token.lexeme[i++] = c;
                }
                token.lexeme[i] = '\0';
                ungetc(c, file);
                
                if (!isValidNumber(token.lexeme)) {
                    token.type = TOKEN_ERROR;
                    strcpy(token.lexeme, "Invalid number format");
                } else {
                    token.type = TOKEN_INT_CONSTANT;
                }
            } else {
                ungetc(c, file);
                token.lexeme[i] = '\0';
                token.type = TOKEN_OPERATOR;
            }
            break;
        case ';':
            token.type = TOKEN_END_OF_LINE;
            strcpy(token.lexeme, ";");
            break;
        case '{':
            token.type = TOKEN_OPEN_BLOCK;
            strcpy(token.lexeme, "{");
            break;
        case '}':
            token.type = TOKEN_CLOSE_BLOCK;
            strcpy(token.lexeme, "}");
            break;
        default:
            token.type = TOKEN_ERROR;
            sprintf(token.lexeme, "Unrecognized character: %c", c);
    }
    return token;
} 