lexer grammar AndroidResourceLexer;

// 关键字和类型
TYPEDEF         : 'typedef';
ENUM            : 'enum';
STRUCT          : 'struct';
UNION           : 'union';
VOID            : 'void';
LOCAL           : 'local';
IF              : 'if';
ELSE            : 'else';
SWITCH          : 'switch';
CASE            : 'case';
BREAK           : 'break';
DEFAULT         : 'default';
RETURN          : 'return';
WHILE           : 'while';
FOR             : 'for';

COMMENT         : '//' ~[\r\n]* -> skip ;
MULTILINE_COMMENT: '/*' .*? '*/' -> skip ;



LPAREN          : '(';
RPAREN          : ')';
LBRACE          : '{';
RBRACE          : '}';
LBRACK          : '[';
RBRACK          : ']';
SEMI            : ';';
COLON           : ':';
COMMA           : ',';
DOT             : '.';
ASSIGN          : '=';
PLUS            : '+';
MINUS           : '-';
STAR            : '*';
DIV             : '/';
MOD             : '%';
AND             : '&';
AND2            : '&&';
OR              : '|';
OR2             : '||';
XOR             : '^';
NOT             : '!';
QUESTION        : '?';
ARROW           : '->';
LSHIFT          : '<<' ;
RSHIFT          : '>>' ;

EQUAL           : '==' ;
NOTEQUAL        : '!=' ;
LT              : '<' ;
LTEQ            : '<=' ;
GT              : '>' ;
GTEQ            : '>=' ;

PLUSPLUS        : '++';
MINUSMINUS      : '--';


PLUSEQ          : '+=';
MINUSEQ         : '-=';
STAREQ          : '*=';
DIVEQ           : '/=';
MODEQ           : '%=';
ANDEQ           : '&=';
OREQ            : '|=';
XOREQ           : '^=';
LSHIFTEQ        : '<<=';
RSHIFTEQ        : '>>=';

// 常量和标识符
HEX_NUMBER      : '0x' [0-9a-fA-F]+ ;
NUMBER          : [0-9]+ ;
IDENTIFIER      : [a-zA-Z_][a-zA-Z0-9_]* ;

// 字符串与字符
fragment ESC_SEQ : '\\' [btnfr"'\\];
STRING          : '"' (ESC_SEQ | ~["\\\r\n])* '"' ;

// 空白
WS              : [ \t\r\n]+ -> skip ;