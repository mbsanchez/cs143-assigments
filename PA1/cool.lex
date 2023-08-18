/*
 *  The scanner definition for COOL.
 */

import java_cup.runtime.Symbol;

%%

%{

/*  Stuff enclosed in %{ %} is copied verbatim to the lexer class
 *  definition, all the extra variables/functions you want to use in the
 *  lexer actions should go here.  Don't remove or modify anything that
 *  was there initially.  */

    // Max size of string constants
    static int MAX_STR_CONST = 1025; 
    int nestedComment = 0;

    // For assembling string constants
    StringBuffer string = new StringBuffer();

    private int curr_lineno = 1;
    int get_curr_lineno() {
	return yyline+1;
    }

    private AbstractSymbol filename;

    void set_filename(String fname) {
	filename = AbstractTable.stringtable.addString(fname);
    }

    AbstractSymbol curr_filename() {
	return filename;
    }
%}

%init{

/*  Stuff enclosed in %init{ %init} is copied verbatim to the lexer
 *  class constructor, all the extra initialization you want to do should
 *  go here.  Don't remove or modify anything that was there initially. */

    // empty for now
%init}

%eofval{

/*  Stuff enclosed in %eofval{ %eofval} specifies java code that is
 *  executed when end-of-file is reached.  If you use multiple lexical
 *  states and want to do something special if an EOF is encountered in
 *  one of those states, place your code in the switch statement.
 *  Ultimately, you should return the EOF symbol, or your lexer won't
 *  work.  */

    switch(yy_lexical_state) {
    case YYINITIAL:
	/* nothing special to do in the initial state */
	break;
	/* If necessary, add code for other states here, e.g:
	   case COMMENT:
	   ...
	   break;
	*/
    case COMMENT:
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "EOF in comment");
    case STRING:
	yybegin(YYINITIAL);
	return new Symbol(TokenConstants.ERROR, "EOF in string");
    }
    return new Symbol(TokenConstants.EOF);
%eofval}

%class CoolLexer
%cup
%line

EndOfLine = \r|\n|\r\n
InputCharacter = [^\r\n|\n|\r]
WhiteSpace = {EndOfLine}|[ \t\f\v\013]
/*Comment = {MultiLineComment} | {OneLineComment}
MultiLineComment = "(*" [^*] ~"*)" | "(*" "*"+ ")"*/
OneLineComment = "--"{InputCharacter}*
TypeId = [A-Z][a-zA-Z0-9_]*
ObjectId = [a-z][a-zA-Z0-9_]*
Integer = [0-9]+
StringCharacter = [^\r\n\"\\]
ScapeNL = [\\]\n

%state STRING, COMMENT, NULLINSTRING

%%

<YYINITIAL>[cC][lL][aA][sS][sS]	{ return new Symbol(TokenConstants.CLASS); }
<YYINITIAL>[eE][lL][sS][eE]	{ return new Symbol(TokenConstants.ELSE); }
<YYINITIAL>[f][aA][lL][sS][eE]	{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(false)); }
<YYINITIAL>[fF][iI]		{ return new Symbol(TokenConstants.FI); }
<YYINITIAL>[iI][fF]		{ return new Symbol(TokenConstants.IF); }
<YYINITIAL>[iI][nN]		{ return new Symbol(TokenConstants.IN); }
<YYINITIAL>[iI][nN][hH][eE][rR][iI][tT][sS]	{ return new Symbol(TokenConstants.INHERITS); }
<YYINITIAL>[iI][sS][vV][oO][iI][dD]		{ return new Symbol(TokenConstants.ISVOID); }
<YYINITIAL>[lL][eE][tT]		{ return new Symbol(TokenConstants.LET); }
<YYINITIAL>[lL][oO][oO][pP]	{ return new Symbol(TokenConstants.LOOP); }
<YYINITIAL>[pP][oO][oO][lL]	{ return new Symbol(TokenConstants.POOL); }
<YYINITIAL>[tT][hH][eE][nN]	{ return new Symbol(TokenConstants.THEN); }
<YYINITIAL>[wW][hH][iI][lL][eE]	{ return new Symbol(TokenConstants.WHILE); }
<YYINITIAL>[cC][aA][sS][eE]	{ return new Symbol(TokenConstants.CASE); }
<YYINITIAL>[oO][fF]		{ return new Symbol(TokenConstants.OF); }
<YYINITIAL>[eE][sS][aA][cC]	{ return new Symbol(TokenConstants.ESAC); }
<YYINITIAL>[nN][eE][wW]		{ return new Symbol(TokenConstants.NEW); }
<YYINITIAL>[nN][oO][tT]		{ return new Symbol(TokenConstants.NOT); }
<YYINITIAL>[t][rR][uU][eE]	{ return new Symbol(TokenConstants.BOOL_CONST, new Boolean(true));  }
<YYINITIAL>"(*"			{ nestedComment = 1; yybegin(COMMENT); }
<YYINITIAL>"*)"			{ return new Symbol(TokenConstants.ERROR, "Unmatched *)"); }
<YYINITIAL>{OneLineComment}	{ /* ignore */ }
<YYINITIAL>{WhiteSpace}		{ /* ignore */ }
<YYINITIAL>"("                  { return new Symbol(TokenConstants.LPAREN); }
<YYINITIAL>")"                  { return new Symbol(TokenConstants.RPAREN); }
<YYINITIAL>"{"                  { return new Symbol(TokenConstants.LBRACE); }
<YYINITIAL>"}"                  { return new Symbol(TokenConstants.RBRACE); }
<YYINITIAL>";"                  { return new Symbol(TokenConstants.SEMI); }
<YYINITIAL>":"                  { return new Symbol(TokenConstants.COLON); }
<YYINITIAL>","                  { return new Symbol(TokenConstants.COMMA); }
<YYINITIAL>"."                  { return new Symbol(TokenConstants.DOT); }
<YYINITIAL>"~"                  { return new Symbol(TokenConstants.NEG); }
<YYINITIAL>\"			{ string.setLength(0); yybegin(STRING); }
<YYINITIAL>"<-"			{ return new Symbol(TokenConstants.ASSIGN); }
<YYINITIAL>"<"                  { return new Symbol(TokenConstants.LT); }
<YYINITIAL>"<="                 { return new Symbol(TokenConstants.LE); }
<YYINITIAL>"="			{ return new Symbol(TokenConstants.EQ); }
<YYINITIAL>"@"			{ return new Symbol(TokenConstants.AT); }
<YYINITIAL>"+"			{ return new Symbol(TokenConstants.PLUS); }
<YYINITIAL>"-"			{ return new Symbol(TokenConstants.MINUS); }
<YYINITIAL>"*"			{ return new Symbol(TokenConstants.MULT); }
<YYINITIAL>"/"			{ return new Symbol(TokenConstants.DIV); }
<YYINITIAL>{TypeId}		{ return new Symbol(TokenConstants.TYPEID, AbstractTable.idtable.addString(yytext())); }
<YYINITIAL>{ObjectId}		{ return new Symbol(TokenConstants.OBJECTID, AbstractTable.idtable.addString(yytext())); }
<YYINITIAL>{Integer}		{ return new Symbol(TokenConstants.INT_CONST, AbstractTable.inttable.addString(yytext())); }


<YYINITIAL>"=>"			{ /* Sample lexical rule for "=>" arrow.
                                     Further lexical rules should be defined
                                     here, after the last %% separator */
                                  return new Symbol(TokenConstants.DARROW); }

<STRING>\"			{ yybegin(YYINITIAL); 
				  if(string.length()>=MAX_STR_CONST)
					return new Symbol(TokenConstants.ERROR, "String constant too long");
			          return new Symbol(TokenConstants.STR_CONST, AbstractTable.stringtable.addString(string.toString(), string.length())); }

<STRING>[\\]"b"                	{ string.append( '\b' ); }
<STRING>[\\]\b                	{ string.append( '\b' ); }
<STRING>[\\]"t"                 { string.append( '\t' ); }
<STRING>[\\]\t                  { string.append( '\t' ); }
<STRING>[\\]"n"                 { string.append( '\n' ); }
<STRING>[\\]"f"                 { string.append( '\f' ); }
<STRING>[\\]\f                  { string.append( '\f' ); }
<STRING>\r                  	{ string.append( '\r' ); }
<STRING>[\\]\033                { string.append( '\033' ); }
<STRING>[\\]"\""                { string.append( '\"' ); }
<STRING>[\\]"\'"                { string.append( '\'' ); }
<STRING>[\\][0-9a-zA-Z\-+]	{ string.append( yytext().charAt(1) ); }
<STRING>[\\][\\]                { string.append( '\\' ); }
<STRING>{ScapeNL}		{ string.append( '\n' ); }
<STRING>\0			{ yybegin(NULLINSTRING); }
<STRING>[\\]\0			{ yybegin(NULLINSTRING); }
<STRING>\n			{ yybegin(YYINITIAL);
				  return new Symbol(TokenConstants.ERROR, "Unterminated string constant"); }
<STRING>{StringCharacter}	{ string.append( yytext() ); }

<NULLINSTRING>\"		{ yybegin(YYINITIAL);
				  return new Symbol(TokenConstants.ERROR, "String contains null character"); }
<NULLINSTRING>[^\n\"]		{  }
<NULLINSTRING>{ScapeNL}		{  }
<NULLINSTRING>\n		{ yybegin(YYINITIAL); 
				  return new Symbol(TokenConstants.ERROR, "String contains null character"); }

<COMMENT>"*)"			{ nestedComment--; 
				  if(nestedComment==0) yybegin(YYINITIAL); }
<COMMENT>"(*"			{ nestedComment++; }
<COMMENT>"*"			{ /*ignore*/ }
<COMMENT>[^*]			{ /*ignore*/ }
<COMMENT>[\\]"*"		{ /*ignore*/ }
<COMMENT>[\\]")"		{ /*ignore*/ }
<COMMENT>[\\]"("		{ /*ignore*/ }

.                               { /* This rule should be the very last
                                     in your lexical specification and
                                     will match match everything not
                                     matched by other lexical rules. */
				return new Symbol(TokenConstants.ERROR, yytext());
                                }
