#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <locale.h>
#include "lexer.h"

// Big Integer Implementation
#define MAX_DIGITS 100

typedef struct {
    char digits[MAX_DIGITS + 1];  // +1 for null terminator
    int sign;  // 1 for positive, -1 for negative
    int length;
} BigInt;

// Parser state
typedef struct {
    FILE* file;
    Token current_token;
    int has_error;
} Parser;

// Variable storage
typedef struct Variable {
    char name[21];
    BigInt value;
    struct Variable* next;
} Variable;

Variable* variables = NULL;

// Function prototypes
void initBigInt(BigInt* bi);
void setBigInt(BigInt* bi, const char* str);
void addBigInt(BigInt* result, const BigInt* a, const BigInt* b);
void subtractBigInt(BigInt* result, const BigInt* a, const BigInt* b);
void printBigInt(const BigInt* bi);
int compareBigInt(const BigInt* a, const BigInt* b);
void copyBigInt(BigInt* dest, const BigInt* src);

// Variable management
Variable* findVariable(const char* name);
Variable* declareVariable(const char* name);
void setVariableValue(const char* name, const BigInt* value);

// Parser functions
void nextToken(Parser* parser);
void parseError(Parser* parser, const char* message);
void parseProgram(Parser* parser);
void parseStatement(Parser* parser);
void parseDeclaration(Parser* parser);
void parseAssignment(Parser* parser);
void parseIncrement(Parser* parser);
void parseDecrement(Parser* parser);
void parseWrite(Parser* parser);
void parseRepeat(Parser* parser);
void parseBlock(Parser* parser);
BigInt parseIntValue(Parser* parser);

// Big Integer Implementation
void initBigInt(BigInt* bi) {
    memset(bi->digits, '0', MAX_DIGITS);
    bi->digits[MAX_DIGITS] = '\0';
    bi->sign = 1;
    bi->length = 1;
}

void setBigInt(BigInt* bi, const char* str) {
    initBigInt(bi);
    int len = strlen(str);
    int start = 0;

    if (str[0] == '-') {
        bi->sign = -1;
        start = 1;
    } else {
        bi->sign = 1;
    }

    int digits_len = len - start;
    if (digits_len > MAX_DIGITS) {
        fprintf(stderr, "Error: Number too large (max %d digits)\n", MAX_DIGITS);
        exit(1);
    }

    bi->length = digits_len;

    // Copy digits in reverse order (least significant first)
    for (int i = 0; i < digits_len; i++) {
        bi->digits[i] = str[len - 1 - i];
    }

    // Remove leading zeros
    while (bi->length > 1 && bi->digits[bi->length - 1] == '0') {
        bi->length--;
    }
}

void printBigInt(const BigInt* bi) {
    if (bi->sign == -1 && !(bi->length == 1 && bi->digits[0] == '0')) {
        printf("-");
    }

    for (int i = bi->length - 1; i >= 0; i--) {
        printf("%c", bi->digits[i]);
    }
}

void copyBigInt(BigInt* dest, const BigInt* src) {
    memcpy(dest->digits, src->digits, MAX_DIGITS + 1);
    dest->sign = src->sign;
    dest->length = src->length;
}

int compareBigInt(const BigInt* a, const BigInt* b) {
    if (a->sign != b->sign) {
        return a->sign - b->sign;
    }

    if (a->length != b->length) {
        return a->sign * (a->length - b->length);
    }

    for (int i = a->length - 1; i >= 0; i--) {
        if (a->digits[i] != b->digits[i]) {
            return a->sign * (a->digits[i] - b->digits[i]);
        }
    }

    return 0;
}

void addBigInt(BigInt* result, const BigInt* a, const BigInt* b) {
    if (a->sign != b->sign) {
        BigInt temp_b;
        copyBigInt(&temp_b, b);
        temp_b.sign = -temp_b.sign;
        subtractBigInt(result, a, &temp_b);
        return;
    }

    initBigInt(result);
    result->sign = a->sign;

    int carry = 0;
    int max_len = (a->length > b->length) ? a->length : b->length;

    for (int i = 0; i < max_len || carry; i++) {
        int sum = carry;
        if (i < a->length) sum += a->digits[i] - '0';
        if (i < b->length) sum += b->digits[i] - '0';

        result->digits[i] = (sum % 10) + '0';
        carry = sum / 10;
        result->length = i + 1;
    }
}

void subtractBigInt(BigInt* result, const BigInt* a, const BigInt* b) {
    if (a->sign != b->sign) {
        BigInt temp_b;
        copyBigInt(&temp_b, b);
        temp_b.sign = -temp_b.sign;
        addBigInt(result, a, &temp_b);
        return;
    }

    const BigInt* larger;
    const BigInt* smaller;

    int cmp = compareBigInt(a, b);
    if (cmp == 0) {
        setBigInt(result, "0");
        return;
    }

    if (cmp > 0) {
        larger = a;
        smaller = b;
        result->sign = a->sign;
    } else {
        larger = b;
        smaller = a;
        result->sign = -a->sign;
    }

    initBigInt(result);
    result->sign = (cmp > 0) ? a->sign : -a->sign;

    int borrow = 0;
    for (int i = 0; i < larger->length; i++) {
        int diff = (larger->digits[i] - '0') - borrow;
        if (i < smaller->length) {
            diff -= (smaller->digits[i] - '0');
        }

        if (diff < 0) {
            diff += 10;
            borrow = 1;
        } else {
            borrow = 0;
        }

        result->digits[i] = diff + '0';
        result->length = i + 1;
    }

    // Remove leading zeros
    while (result->length > 1 && result->digits[result->length - 1] == '0') {
        result->length--;
    }
}

// Variable Management
Variable* findVariable(const char* name) {
    Variable* current = variables;
    while (current) {
        if (strcmp(current->name, name) == 0) {
            return current;
        }
        current = current->next;
    }
    return NULL;
}

Variable* declareVariable(const char* name) {
    if (findVariable(name)) {
        return NULL; // Already declared
    }

    Variable* var = (Variable*)malloc(sizeof(Variable));
    strcpy(var->name, name);
    setBigInt(&var->value, "0");
    var->next = variables;
    variables = var;
    return var;
}

void setVariableValue(const char* name, const BigInt* value) {
    Variable* var = findVariable(name);
    if (var) {
        copyBigInt(&var->value, value);
    }
}

// Parser Implementation
void nextToken(Parser* parser) {
    parser->current_token = getNextToken(parser->file);
    if (parser->current_token.type == TOKEN_ERROR) {
        parseError(parser, parser->current_token.lexeme);
    }
}

void parseError(Parser* parser, const char* message) {
    fprintf(stderr, "Error at line %d: %s\n", parser->current_token.line_number, message);
    parser->has_error = 1;
    exit(1);
}

BigInt parseIntValue(Parser* parser) {
    BigInt result;
    
    if (parser->current_token.type == TOKEN_INT_CONSTANT) {
        setBigInt(&result, parser->current_token.lexeme);
        nextToken(parser);
    } else if (parser->current_token.type == TOKEN_IDENTIFIER) {
        Variable* var = findVariable(parser->current_token.lexeme);
        if (!var) {
            parseError(parser, "Undeclared variable");
        }
        copyBigInt(&result, &var->value);
        nextToken(parser);
    } else {
        parseError(parser, "Expected integer value or variable");
    }
    
    return result;
}

void parseDeclaration(Parser* parser) {
    // Expect 'number' keyword
    if (parser->current_token.type != TOKEN_KEYWORD || 
        strcmp(parser->current_token.lexeme, "number") != 0) {
        parseError(parser, "Expected 'number' keyword");
    }
    nextToken(parser);
    
    // Expect identifier
    if (parser->current_token.type != TOKEN_IDENTIFIER) {
        parseError(parser, "Expected variable name");
    }
    
    if (!declareVariable(parser->current_token.lexeme)) {
        parseError(parser, "Variable already declared");
    }
    
    nextToken(parser);
    
    // Expect semicolon
    if (parser->current_token.type != TOKEN_END_OF_LINE) {
        parseError(parser, "Expected ';'");
    }
    nextToken(parser);
}

void parseAssignment(Parser* parser) {
    char var_name[21];
    strcpy(var_name, parser->current_token.lexeme);
    
    Variable* var = findVariable(var_name);
    if (!var) {
        parseError(parser, "Undeclared variable");
    }
    
    nextToken(parser);
    
    // Expect ':='
    if (parser->current_token.type != TOKEN_OPERATOR || 
        strcmp(parser->current_token.lexeme, ":=") != 0) {
        parseError(parser, "Expected ':='");
    }
    nextToken(parser);
    
    BigInt value = parseIntValue(parser);
    setVariableValue(var_name, &value);
    
    // Expect semicolon
    if (parser->current_token.type != TOKEN_END_OF_LINE) {
        parseError(parser, "Expected ';'");
    }
    nextToken(parser);
}

void parseIncrement(Parser* parser) {
    char var_name[21];
    strcpy(var_name, parser->current_token.lexeme);
    
    Variable* var = findVariable(var_name);
    if (!var) {
        parseError(parser, "Undeclared variable");
    }
    
    nextToken(parser);
    
    // Expect '+='
    if (parser->current_token.type != TOKEN_OPERATOR || 
        strcmp(parser->current_token.lexeme, "+=") != 0) {
        parseError(parser, "Expected '+='");
    }
    nextToken(parser);
    
    BigInt increment_value = parseIntValue(parser);
    BigInt result;
    addBigInt(&result, &var->value, &increment_value);
    setVariableValue(var_name, &result);
    
    // Expect semicolon
    if (parser->current_token.type != TOKEN_END_OF_LINE) {
        parseError(parser, "Expected ';'");
    }
    nextToken(parser);
}

void parseDecrement(Parser* parser) {
    char var_name[21];
    strcpy(var_name, parser->current_token.lexeme);
    
    Variable* var = findVariable(var_name);
    if (!var) {
        parseError(parser, "Undeclared variable");
    }
    
    nextToken(parser);
    
    // Expect '-='
    if (parser->current_token.type != TOKEN_OPERATOR || 
        strcmp(parser->current_token.lexeme, "-=") != 0) {
        parseError(parser, "Expected '-='");
    }
    nextToken(parser);
    
    BigInt decrement_value = parseIntValue(parser);
    BigInt result;
    subtractBigInt(&result, &var->value, &decrement_value);
    setVariableValue(var_name, &result);
    
    // Expect semicolon
    if (parser->current_token.type != TOKEN_END_OF_LINE) {
        parseError(parser, "Expected ';'");
    }
    nextToken(parser);
}

void parseWrite(Parser* parser) {
    // Expect 'write' keyword
    if (parser->current_token.type != TOKEN_KEYWORD || 
        strcmp(parser->current_token.lexeme, "write") != 0) {
        parseError(parser, "Expected 'write' keyword");
    }
    nextToken(parser);
    
    // Parse output list
    do {
        if (parser->current_token.type == TOKEN_STRING_CONSTANT) {
            printf("%s", parser->current_token.lexeme);
            nextToken(parser);
        } else if (parser->current_token.type == TOKEN_INT_CONSTANT) {
            BigInt value;
            setBigInt(&value, parser->current_token.lexeme);
            printBigInt(&value);
            nextToken(parser);
        } else if (parser->current_token.type == TOKEN_IDENTIFIER) {
            Variable* var = findVariable(parser->current_token.lexeme);
            if (!var) {
                parseError(parser, "Undeclared variable");
            }
            printBigInt(&var->value);
            nextToken(parser);
        } else if (parser->current_token.type == TOKEN_KEYWORD && 
                   strcmp(parser->current_token.lexeme, "newline") == 0) {
            printf("\n");
            nextToken(parser);
        } else {
            parseError(parser, "Expected string, number, variable, or newline");
        }
        
        // Check for 'and' keyword
        if (parser->current_token.type == TOKEN_KEYWORD && 
            strcmp(parser->current_token.lexeme, "and") == 0) {
            nextToken(parser);
        } else {
            break;
        }
    } while (1);
    
    // Expect semicolon
    if (parser->current_token.type != TOKEN_END_OF_LINE) {
        parseError(parser, "Expected ';'");
    }
    nextToken(parser);
}

void parseRepeat(Parser* parser) {
    // Expect 'repeat' keyword
    if (parser->current_token.type != TOKEN_KEYWORD || 
        strcmp(parser->current_token.lexeme, "repeat") != 0) {
        parseError(parser, "Expected 'repeat' keyword");
    }
    nextToken(parser);
    
    // Check if it's a constant or variable
    int is_variable_loop = 0;
    int loop_count = 0;
    char loop_var_name[21] = "";
    Variable* loop_var = NULL;
    
    if (parser->current_token.type == TOKEN_INT_CONSTANT) {
        // Constant loop
        loop_count = atoi(parser->current_token.lexeme);
        if (loop_count < 0) {
            parseError(parser, "Loop count cannot be negative");
        }
        nextToken(parser);
    } else if (parser->current_token.type == TOKEN_IDENTIFIER) {
        // Variable loop
        is_variable_loop = 1;
        strcpy(loop_var_name, parser->current_token.lexeme);
        loop_var = findVariable(loop_var_name);
        if (!loop_var) {
            parseError(parser, "Undeclared loop variable");
        }
        
        // Convert BigInt to int for loop counting
        // Check if variable is positive and reasonable for looping
        if (loop_var->value.sign == -1) {
            parseError(parser, "Loop variable cannot be negative");
        }
        
        // Convert BigInt to int (check for reasonable size)
        if (loop_var->value.length > 9) {  // More than 9 digits = too large
            parseError(parser, "Loop variable too large");
        }
        
        loop_count = 0;
        for (int i = loop_var->value.length - 1; i >= 0; i--) {
            loop_count = loop_count * 10 + (loop_var->value.digits[i] - '0');
        }
        
        nextToken(parser);
    } else {
        parseError(parser, "Expected number or variable after 'repeat'");
    }
    
    // Expect 'times' keyword
    if (parser->current_token.type != TOKEN_KEYWORD || 
        strcmp(parser->current_token.lexeme, "times") != 0) {
        parseError(parser, "Expected 'times' keyword");
    }
    nextToken(parser);
    
    // Execute loop (both constant and variable loops use same structure)
    if (loop_count <= 0) {
        // Skip loop body without executing it
        if (parser->current_token.type == TOKEN_OPEN_BLOCK) {
            int brace_count = 1;
            nextToken(parser); // consume '{'
            while (brace_count > 0 && parser->current_token.type != TOKEN_EOF) {
                if (parser->current_token.type == TOKEN_OPEN_BLOCK) {
                    brace_count++;
                } else if (parser->current_token.type == TOKEN_CLOSE_BLOCK) {
                    brace_count--;
                }
                nextToken(parser);
            }
        } else {
            // Skip single statement
            while (parser->current_token.type != TOKEN_END_OF_LINE && 
                   parser->current_token.type != TOKEN_EOF) {
                nextToken(parser);
            }
            if (parser->current_token.type == TOKEN_END_OF_LINE) {
                nextToken(parser);
            }
        }
        
        // For variable loops, set variable to 0
        if (is_variable_loop) {
            BigInt zero_value;
            initBigInt(&zero_value);
            setBigInt(&zero_value, "0");
            copyBigInt(&loop_var->value, &zero_value);
        }
        return;
    }
    
    // Execute loop normally
    for (int i = 0; i < loop_count; i++) {
        // Save file position for this iteration
        long body_start = ftell(parser->file);
        Token body_start_token = parser->current_token;
        
        // Execute loop body
        if (parser->current_token.type == TOKEN_OPEN_BLOCK) {
            parseBlock(parser);
        } else {
            parseStatement(parser);
        }
        
        // For variable loops, decrement the variable after each iteration
        if (is_variable_loop) {
            BigInt one_value;
            initBigInt(&one_value);
            setBigInt(&one_value, "1");
            BigInt result;
            subtractBigInt(&result, &loop_var->value, &one_value);
            copyBigInt(&loop_var->value, &result);
        }
        
        // Reset file position for next iteration (except last)
        if (i < loop_count - 1) {
            fseek(parser->file, body_start, SEEK_SET);
            parser->current_token = body_start_token;
        }
    }
    
    // For variable loops, set variable to 0 after completion
    if (is_variable_loop) {
        BigInt zero_value;
        initBigInt(&zero_value);
        setBigInt(&zero_value, "0");
        copyBigInt(&loop_var->value, &zero_value);
    }
}

void parseBlock(Parser* parser) {
    // Expect '{'
    if (parser->current_token.type != TOKEN_OPEN_BLOCK) {
        parseError(parser, "Expected '{'");
    }
    nextToken(parser);
    
    // Check for empty block
    if (parser->current_token.type == TOKEN_CLOSE_BLOCK) {
        parseError(parser, "Empty blocks are not allowed");
    }
    
    // Parse statements until '}'
    while (parser->current_token.type != TOKEN_CLOSE_BLOCK && 
           parser->current_token.type != TOKEN_EOF) {
        parseStatement(parser);
    }
    
    // Expect '}'
    if (parser->current_token.type != TOKEN_CLOSE_BLOCK) {
        parseError(parser, "Expected '}'");
    }
    nextToken(parser);
}

void parseStatement(Parser* parser) {
    if (parser->current_token.type == TOKEN_KEYWORD) {
        if (strcmp(parser->current_token.lexeme, "number") == 0) {
            parseDeclaration(parser);
        } else if (strcmp(parser->current_token.lexeme, "write") == 0) {
            parseWrite(parser);
        } else if (strcmp(parser->current_token.lexeme, "repeat") == 0) {
            parseRepeat(parser);
        } else {
            parseError(parser, "Unexpected keyword");
        }
    } else if (parser->current_token.type == TOKEN_IDENTIFIER) {
        // Look ahead to determine statement type
        Token saved_token = parser->current_token;
        long saved_pos = ftell(parser->file);
        
        nextToken(parser);
        
        if (parser->current_token.type == TOKEN_OPERATOR) {
            if (strcmp(parser->current_token.lexeme, ":=") == 0) {
                // Reset and parse assignment
                fseek(parser->file, saved_pos, SEEK_SET);
                parser->current_token = saved_token;
                parseAssignment(parser);
            } else if (strcmp(parser->current_token.lexeme, "+=") == 0) {
                // Reset and parse increment
                fseek(parser->file, saved_pos, SEEK_SET);
                parser->current_token = saved_token;
                parseIncrement(parser);
            } else if (strcmp(parser->current_token.lexeme, "-=") == 0) {
                // Reset and parse decrement
                fseek(parser->file, saved_pos, SEEK_SET);
                parser->current_token = saved_token;
                parseDecrement(parser);
            } else {
                parseError(parser, "Invalid operator");
            }
        } else {
            parseError(parser, "Expected operator after identifier");
        }
    } else if (parser->current_token.type == TOKEN_OPEN_BLOCK) {
        parseBlock(parser);
    } else {
        parseError(parser, "Invalid statement");
    }
}

void parseProgram(Parser* parser) {
    nextToken(parser); // Get first token
    
    while (parser->current_token.type != TOKEN_EOF && !parser->has_error) {
        parseStatement(parser);
    }
}

// Main function
int main(int argc, char* argv[]) {
    char filename[256];
    
    if (argc < 2) {
        printf("Enter script filename (without .ppp extension): ");
        if (scanf("%255s", filename) != 1) {
            fprintf(stderr, "Error reading filename\n");
            return 1;
        }
    } else {
        strcpy(filename, argv[1]);
    }
    
    // Add .ppp extension if not present
    if (strstr(filename, ".ppp") == NULL) {
        strcat(filename, ".ppp");
    }
    
    FILE* file = fopen(filename, "r");
    if (!file) {
        fprintf(stderr, "Error: Cannot open file %s\n", filename);
        return 1;
    }
    
    Parser parser;
    parser.file = file;
    parser.has_error = 0;
    
    parseProgram(&parser);
    
    if (!parser.has_error) {
        printf("\nProgram executed successfully.\n");
    }
    
    fclose(file);
    
    // Free variables
    while (variables) {
        Variable* temp = variables;
        variables = variables->next;
        free(temp);
    }
    
    return parser.has_error ? 1 : 0;
}