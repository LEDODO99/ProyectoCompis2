grammar DECAF;

ID : ([a-z]|[A-Z]) ([a-z]|[A-Z]|[0-9])* ;
NUM: [0-9]+;
CHAR: [a-z];
WS : [ \t\r\n\f]+  ->channel(HIDDEN);

program: 'class' 'Program' '{' (declaration)* '}' ;
declaration: structDeclaration
	| varDeclaration
	| methodDeclaration 
	;
varDeclaration : varType ID ';' | varType ID '[' NUM ']' ';' ;
structDeclaration : 'struct' ID '{' (varDeclaration)* '}' ';' ;
varType : 'int'
	| 'char'
	| 'boolean'
	| 'struct' ID
	| 'void'
	;
methodDeclaration : methodType ID '(' ('void'|(parameter)*) ')' block ;
methodType : 'int'
	| 'char'
	| 'boolean'
	| 'void'
	;
parameter : parameterType ID
	| parameterType ID '[' ']'
	;
parameterType : 'int'
	| 'char'
	| 'boolean'
	;
block : '{' (varDeclaration)* (statement)* '}' ;
statement : ifStatement
	| whileStatement
	| returnStatement
	| methodCall ';'
	| block
	| assignment
	| (expression)? ';'
	;
location : (ID | ID '[' expression ']') ('.' location)? ;
expression : location 
	| methodCall
	| literal
	| expression op expression
	| '-' expression
	| '!' expression
	| '{' expression '}'
	;
returnStatement: 'return' (expression)? ';';
ifStatement: 'if' '(' expression ')' block (elseStatement)? ;
elseStatement: 'else' block;
whileStatement: 'while' '(' expression ')' block ;
methodCall : ID '(' (arg)* ')' ;
arg : expression ;
op : arith_op
	| rel_op
	| eq_op
	| cond_op
	;
arith_op : '+' | '-' | '*' | '/' | '%' ;
rel_op : '<' | '>' | '<=' | '>=' ;
eq_op :  '==' | '!=' ;
cond_op : '&&' | '||' ;
literal : int_literal
	| char_literal
	| bool_literal
	;
int_literal : NUM ;
char_literal :  '\'' CHAR '\'' ;
bool_literal : 'true' | 'false' ;
assignment : location '=' expression ;