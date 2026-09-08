parser grammar AndroidResourceParser;

options { tokenVocab=AndroidResourceLexer; }

file
    : (statement)*
    ;




statement
    : typedefDecl
    | enumDecl
    | structDecl
    | funcDecl
    | assignStmt
    | returnStmt
    | exprStmt
    | varDecl
    | ifStmt
    | forStmt
    | whileStmt
    | switchStmt
    | breakStmt

//    | SEMI
    ;

breakStmt
    : BREAK SEMI
    ;

whileStmt
    : WHILE LPAREN expr RPAREN block
    ;

switchStmt
    : SWITCH LPAREN expr RPAREN LBRACE switchBlockStatementGroup* switchLabel* RBRACE
    ;

switchBlockStatementGroup
    : switchLabel+ (statement* | block)
    ;

switchLabel
    : CASE expr COLON
    | DEFAULT COLON
    ;

forStmt
    : FOR LPAREN (assignStmt | exprStmt | ';') expr? SEMI expr? RPAREN statementOrBlock
    ;

exprStmt
    : expr SEMI
    ;


ifStmt
    : IF LPAREN expr RPAREN statementOrBlock (ELSE statementOrBlock)?
    ;

statementOrBlock:
    statement | LBRACE statement* RBRACE
    ;

block
    : LBRACE statement* RBRACE
    ;

returnStmt
    : RETURN expr? SEMI
    ;

typedefDecl
    : TYPEDEF (enumDecl | structDecl | unionDecl | type ) IDENTIFIER fieldAttributes? SEMI
    ;

enumDecl
    : ENUM (LT type GT)? LBRACE enumItemList RBRACE (IDENTIFIER)? SEMI?
    ;

enumItemList
    : enumItem (COMMA enumItem)* (COMMA)?
    ;

enumItem
    : assignStmt | IDENTIFIER
    ;

structDecl
    : STRUCT IDENTIFIER? (LPAREN paramList RPAREN)? block fieldAttributes? SEMI?
    ;


structItemList
    : structItem*
    ;

structItem
    : typedefDecl
    | structDecl
    | varDecl
    | assignStmt
    | enumDecl
    | unionDecl
    ;

unionDecl
    : UNION LBRACE structItemList RBRACE IDENTIFIER? SEMI?
    ;

varDecl
    : type IDENTIFIER (LPAREN argList? RPAREN)? (LBRACK expr RBRACK)? (ASSIGN expr)? fieldAttributes? SEMI
    | LOCAL varDecl
    ;

argList
    : expr (COMMA expr)*
    ;

funcCall
    : IDENTIFIER LPAREN (argList)? RPAREN
    ;

type
    : STRUCT IDENTIFIER
    | IDENTIFIER (LT IDENTIFIER (COMMA IDENTIFIER)* GT)?
    | VOID
    ;

funcDecl
    : type IDENTIFIER LPAREN paramList? RPAREN funcBody
    ;

funcBody
    : LBRACE statement* RBRACE
    ;

paramList
    : param (COMMA param)*
    ;

param
    : type (AND | STAR)? IDENTIFIER
    ;




assignStmt
    : IDENTIFIER (ASSIGN | PLUSEQ | MINUSEQ | STAREQ | DIVEQ | MODEQ | ANDEQ | OREQ | XOREQ | LSHIFTEQ | RSHIFTEQ) expr SEMI?
    ;


expr
    : expr (PLUS|MINUS|STAR|DIV|MOD|LSHIFT|RSHIFT|AND|OR) expr
    | expr (EQUAL | NOTEQUAL | LT | LTEQ | GT | GTEQ | AND2 | OR2) expr
    | (AND | STAR | PLUS | MINUS | NOT) expr
    | expr LBRACK expr RBRACK
    | funcCall
    | LPAREN expr RPAREN
    | IDENTIFIER
    | NUMBER
    | HEX_NUMBER
    | STRING
    | expr DOT expr
    | expr (PLUSPLUS | MINUSMINUS)
    | (PLUSPLUS | MINUSMINUS) expr
    ;

fieldAttributes
    : LT attribute (COMMA attribute)* GT
    ;

attribute
    : IDENTIFIER ASSIGN expr
    ;