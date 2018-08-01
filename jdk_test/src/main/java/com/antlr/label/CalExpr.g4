grammar CalExpr;

prog : stat+;

stat :expr NEWLINE        #printExpr
    |ID '=' expr NEWLINE  #assign
    |NEWLINE              #blank
    ;

expr : expr op=('+'|'-') expr   # AddSub
    | expr op=('*'|'/') expr    # MulDiv
    | ID                        # id
    | INT                       # int
    | '(' expr ')'              # parens
    ;

ID : [a-zA-Z]+ ;
INT : [0-9]+ ;
NEWLINE : '\r' ? '\n';
WS : [ \t]+ -> skip;

ADD : '+' ;
SUB : '-' ;
MUL : '*' ;
DIV : '/' ;