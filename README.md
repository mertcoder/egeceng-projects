# 🎓 Ege University Projects

This repository contains my software projects developed at Ege University.

---

## 📋 Projects

### 🚌 Online Reservation System
**Technology:** Java  
**Description:** A comprehensive desktop application developed for travel reservations

**Features:**
- 👤 User registration and login system
- 🔍 Trip search and filtering
- 💺 Seat selection
- 📊 Admin panel and management tools
- 💳 Reservation confirmation system
- 👥 User account management

**Architecture:** Layered architecture (UI, Service, DAO layers)  
**OOP Compliance:** Follows core OOP principles (Encapsulation, Inheritance, Polymorphism, Abstraction)  
**Design Patterns:** Factory Pattern, Singleton Pattern, Builder Pattern, Strategy Pattern  
**Testing:** JUnit 5 unit tests for critical components (Strategy Pattern, Factory Pattern, Input Validation)

### 🔤 Plus++ Language Interpreter
**Technology:** C  
**Description:** A complete interpreter for the Plus++ programming language with big integer arithmetic support

**Language Features:**
- 📊 Big integer arithmetic (up to 100 decimal digits)
- 🔄 Variable declarations and assignments (`:=`)
- ➕ Increment/decrement operations (`+=`, `-=`)
- 🔁 Loop statements (`repeat n times`)
- 📤 Output statements with string formatting (`write`)
- 💬 Multi-line comment support (`*comment*`)
- 🏗️ Code blocks with nested structures (`{}`)

**Implementation Components:**
- **Lexer:** Tokenizes source code and validates syntax
  - Token types: `KEYWORD`, `IDENTIFIER`, `INT_CONSTANT`, `STRING_CONSTANT`, `OPERATOR`, `END_OF_LINE`, `OPEN_BLOCK`, `CLOSE_BLOCK`, `ERROR`, `EOF`
  - Input validation for numbers, identifiers, and string literals
  - Multi-line comment processing
- **Parser:** Recursive descent parser with error detection
  - Grammar-based parsing for statements, expressions, and blocks
  - Syntax error detection with line number reporting
  - Left-to-right single-pass parsing with look-ahead
- **Big Integer Engine:** Custom arbitrary precision arithmetic
  - Digit-by-digit arithmetic operations (addition, subtraction)
  - Sign handling for positive/negative numbers
  - Memory-efficient storage in character arrays
- **Symbol Table:** Variable management and scope handling
  - Linked list-based variable storage
  - Global scope with automatic initialization to zero
  - Variable name validation (20 char max, alphanumeric + underscore)
- **Error System:** Comprehensive error detection and reporting
  - Lexical errors (invalid tokens, malformed numbers)
  - Syntax errors (grammar violations, missing semicolons)
  - Semantic errors (undeclared variables, type mismatches)

**Technical Highlights:**
- Custom big integer implementation supporting 100+ digit numbers
- Recursive descent parsing with look-ahead
- Memory-efficient variable storage with linked lists
- Comprehensive test suite with edge cases and error scenarios
- Command-line interface (`ppp filename.ppp`)

---

## 📚 License

These projects are developed for educational purposes. 