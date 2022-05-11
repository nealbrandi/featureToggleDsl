grammar FT;

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];

KW_ACTIVATE_ALL  : (A C T I V A T E) WHITESPACE (A L L);
KW_AND           : A N D;
KW_OR            : O R;
KW_NOT           : N O T;
KW_CLUSTER       : C L U S T E R;
KW_MACHINE       : M A C H I N E;
KW_STAGE         : S T A G E;
KW_TAG           : T A G;
KW_FIRM          : F I R M;
KW_DEPARTMENT    : D E P A R T M E N T;
KW_UUID          : U U I D;

OPEN_BRACKET     : '[';
CLOSE_BRACKET    : ']';
OPEN_PAREN       : '(';
CLOSE_PAREN      : ')';
COMMA            : ',';
INTERSECTION     : '^';
UNION            : '+';
SUBTRACTION      : '-';

INTEGER          : DIGIT+ ;
IDENTIFIER       : CHARACTER (CHARACTER | DIGIT)* ;
DIGIT            : [0-9] ;
CHARACTER        : [a-zA-Z] ;
WHITESPACE       : [ \t\r\n] -> skip ;

IDENTIFIER_LIST  : OPEN_BRACKET IDENTIFIER (COMMA WHITESPACE? IDENTIFIER)* CLOSE_BRACKET ;
INTEGER_LIST     : OPEN_BRACKET INTEGER    (COMMA WHITESPACE? INTEGER)*    CLOSE_BRACKET ;

locationActivationTerm
    : locationType=KW_CLUSTER exclude=KW_NOT? value=IDENTIFIER_LIST
    | locationType=KW_MACHINE exclude=KW_NOT? value=IDENTIFIER_LIST
    | locationType=KW_STAGE   exclude=KW_NOT? value=INTEGER_LIST
    | locationType=KW_TAG     exclude=KW_NOT? value=IDENTIFIER_LIST
    | locationType=KW_CLUSTER exclude=KW_NOT? value=IDENTIFIER
    | locationType=KW_MACHINE exclude=KW_NOT? value=IDENTIFIER
    | locationType=KW_STAGE   exclude=KW_NOT? value=INTEGER
    | locationType=KW_TAG     exclude=KW_NOT? value=IDENTIFIER
    ;

userActivationTerm
    : userType=KW_FIRM       exclude=KW_NOT? value=INTEGER_LIST
    | userType=KW_DEPARTMENT exclude=KW_NOT? value=IDENTIFIER_LIST
    | userType=KW_UUID       exclude=KW_NOT? value=INTEGER_LIST
    | userType=KW_FIRM       exclude=KW_NOT? value=INTEGER
    | userType=KW_DEPARTMENT exclude=KW_NOT? value=IDENTIFIER
    | userType=KW_UUID       exclude=KW_NOT? value=INTEGER
    ;

activationRule
    : KW_ACTIVATE_ALL EOF
    | activationTerm+ EOF
    ;

activationTerm
    : OPEN_PAREN inner=activationTerm CLOSE_PAREN                                                            # ParenthesizedActivationTerm
    | left=locationActivationTerm operator=(INTERSECTION | UNION | SUBTRACTION) right=locationActivationTerm # LocationSetOperator
    | left=userActivationTerm     operator=(INTERSECTION | UNION | SUBTRACTION) right=userActivationTerm     # UserSetOperator
    | left=activationTerm         operator=(KW_AND | KW_OR)                     right=activationTerm         # CompoundLogicalActivationTerm 
    | locationActivationTerm                                                                                 # LocationTerm
    | userActivationTerm                                                                                     # UserTerm
    ;


