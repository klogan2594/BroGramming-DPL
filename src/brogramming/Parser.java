package brogramming;

import java.io.FileNotFoundException;
import java.io.IOException;
import static brogramming.BroGramming.fatal;

public class Parser {
	static Lexeme tree;
	static Lexeme curTree;
	

	public Parser() throws FileNotFoundException, IOException {
		curTree = Lexer.tree; //should be tree instead of curTree
	}
	

//----------GRAMMAR FUNCS----------//

	
	public Lexeme progDef() {
		Lexeme def = definition();
		if (progDefPending()) { Lexeme program = progDef(); return cons("PROGRAM", def, cons("JOIN", program, null)); }
		return cons("PROGRAM", def, null);
	}
	
	
	public Lexeme definition() {
		if (varDefPending()) { return cons("DEFINITON", varDef(), null); }
		
		else if (funcDefPending()) { return cons("DEFINITION", funcDef(), null); }
		
		else if (exprPending()) { return cons("DEFINITION", expr(), null); }
		
		else { return null; }
	}
	
	
	public Lexeme varDef() {
		return cons("VARDEF", match("TYPE"), cons("JOIN", match("ID"), cons("JOIN", match("EQUAL"), cons("JOIN", expr(), cons("JOIN",match("SEMI"), null)))));
	}
	
	//DONE
	public Lexeme funcDef() {
		Lexeme func =  match("FUNC");
		Lexeme id = match("ID");
		if (check("EQUAL")) {
			Lexeme equal = match("EQUAL");
			if (check("ID")) { 
				return cons("FDEF", func, cons("JOIN", id, cons("JOIN", equal, cons("JOIN", match("ID"), cons("JOIN", match("SEMI"), null))))); 
			}
			else {
				Lexeme lam = lambda();
				Lexeme semi = match("SEMI");
				Lexeme op = lam.right.right.left;
				Lexeme b = lam.right.right.right.right.left;	
				return cons("FDEF", func, cons("JOIN", id, cons("JOIN", null, cons("JOIN", op, cons("JOIN", null, cons("JOIN", b, null))))));
			}
		}
		else { 
			Lexeme oparen = match("OPAREN");
			Lexeme optParamList = optParamList();
			Lexeme cparen = match("CPAREN");
			Lexeme block = block();
			return cons("FDEF", func, cons("JOIN", id, cons("JOIN", oparen, cons("JOIN", optParamList, cons("JOIN", cparen, cons("JOIN", block, null))))));
		}
	}
	
    public Lexeme idDef() {
		Lexeme id = match("ID");
		if (check("OPAREN")) { return cons("FUNCCALL", id, cons("JOIN", match("OPAREN"), cons("JOIN", optExprList(), cons("JOIN", match("CPAREN"), null)))); }
		
		else if (check("OBRACKET")) { return cons("ARRAYACCESS", id, cons("JOIN", match("OBRACKET"), cons("JOIN", expr(), cons("JOIN", match("CBRACKET"), null)))); } 
		
		else { return cons("IDDEF", id, null); }
    }

    
    public Lexeme optParamList() {
		if (paramListPending()) { return cons("OPTPARAMLIST", paramList(), null); }
		else { return cons("OPTPARAMLIST", null, null); }
    }
    
    public Lexeme paramList() {
		Lexeme id = match("ID");
		if (check("COMMA")){
		    Lexeme comma = match("COMMA");
		    return cons("PARAMLIST", id, paramList());
		}
		return cons("PARAMLIST", id, null);
    }
    
    
    public Lexeme expr() {
		Lexeme unary = unary();
		
		if (opPending()) {
			Lexeme op = op();
			Lexeme expr = expr();
		    return cons("EXPR", new Lexeme(op.type, op.type, unary, expr),null);
		}     
	    return cons("EXPR", unary, null);
    } 
    
    
    public Lexeme exprList() {
		Lexeme expr = expr();
		if (check("COMMA")){
		    Lexeme comma = match("COMMA");
		    return cons("EXPRLIST", expr, exprList());
		}
		return cons("EXPRLIST", expr, null);
    }
    
    
    public Lexeme optExprList() {
    	if(exprListPending()){ return cons("OPTEXPRLIST", exprList(), null); }	    
    	else { return cons("OPTEXPRLIST", null, null); }
    }
    	
    public Lexeme optStatementList() {
    	if (statementListPending()) { return cons("OPTSTATEMENTLIST", statementList(), null); }
    	else { return cons("OPTSTATEMENTLIST", null, null); }
    }
    
    public Lexeme statementList() {
		Lexeme statement = statement();
		if(statementListPending()){ return cons("STATEMENTLIST", statement, cons("JOIN", statementList(), null)); }
		else { return cons("STATEMENTLIST", statement, null); }
	} 
    

    public Lexeme statement() {
		if(varDefPending()) { return cons("STATEMENT", varDef(), null); }
		
		else if(funcDefPending()) { return cons("STATEMENT", funcDef(), null); }
		
		else if(exprPending()) { return cons("STATEMENT", expr(), cons("JOIN", match("SEMI"), null)); }
		
		else if(whileLoopPending()) { return cons("STATEMENT", whileLoop(), null); }
		
		else if(ifStatementPending()) { return cons("STATEMENT", ifStatement(), null); }
		
		else if(check("RETURN")) { return cons("STATEMENT", match("RETURN") ,cons("JOIN", expr(), cons("JOIN", match("SEMI"), null))); }
		
		else { return null; }
    }
    
    
    public Lexeme whileLoop() { return cons("WHILELOOP", match("WHILE") ,cons("JOIN", match("OPAREN"), cons("JOIN", expr(), cons("JOIN", match("CPAREN"), cons("JOIN", block(), null))))); }

    public Lexeme ifStatement() { return cons("IFSTATEMENT", match("IF") ,cons("JOIN", match("OPAREN"), cons("JOIN", expr(), cons("JOIN", match("CPAREN"), cons("JOIN", block(), cons("JOIN", optElseStatement(), null)))))); }
    
    public Lexeme optElseStatement() {
    	if (elseStatementPending()){ return cons("OPTELSESTATEMENT", elseStatement(), null); }
    	else { return cons("OPTELSESTATEMENT", null, null); }
    }
    
    
    public Lexeme elseStatement() {
		Lexeme elseStatement = match("ELSE");
		if (blockPending()) { return cons("ELSESTATEMENT", elseStatement, cons("JOIN", block(), null)); }
		else if (ifStatementPending()) { return cons("ELSESTATE", elseStatement, cons("JOIN", ifStatement(), null)); }
		else  { return null; } 
    }
    
    public Lexeme lambda(){ return cons("LAMBDA", match("LAMBDA") ,cons("JOIN", match("OPAREN"), cons("JOIN", optParamList(), cons("JOIN", match("CPAREN"), cons("JOIN", block(), null))))); }
	  
    public Lexeme block() { return cons("BLOCK", match("OBRACE"), cons("JOIN", optStatementList(), cons("JOIN", match("CBRACE"), null))); }
    

    public Lexeme unary() {
		if (idDefPending()) { return cons("UNARY", idDef(), null); }
		
		else if(check("STRING")) { return cons("UNARY", match("STRING"), null); }
		
		else if(check("INTEGER")) { return cons("UNARY", match("INTEGER"), null); }
		
		else if(check("REAL")) { return cons("UNARY", match("REAL"), null); }
		
		else if (check("OPAREN")) { return cons("UNARY", match("OPAREN"), cons("JOIN", expr(), cons("JOIN", match("CPAREN"), null))); }
		
		else if (check("OBRACKET")) { return cons("UNARY",  match("OBRACKET"), cons("JOIN", optExprList(), cons("JOIN", match("CBRACKET"), null))); }
		
		else if (check("NIL")) { return cons("UNARY", match("NIL"), null); }
		
		else if (check("BOOLEAN")) { return cons("UNARY", match("BOOLEAN"), null);}
		
		else if (check("NOT")) { return cons("UNARY", match("NOT"), cons("JOIN", unary(), null)); }
		
		else if (check("BREAK")) { return match("BREAK"); }
		
		else if (lambdaPending()) { return cons("UNARY", lambda(), null); }
		
		else if (funcDefPending()) { return cons("UNARY", funcDef(), null); }
		
		else if (check("INSERT")) {
		    Lexeme insert = match("INSERT");
		    Lexeme oparen = match("OPAREN");
		    Lexeme exprList = exprList();
		    Lexeme cparen = match("CPAREN");
		    return cons("INSERT", insert, cons("JOIN", exprList, null));
		}
		
		else if (check("APPEND")) {
		    Lexeme append = match("APPEND");
		    Lexeme oparen = match("OPAREN");
		    Lexeme exprList = exprList();
		    Lexeme cparen = match("CPAREN");
		    return cons("APPEND", append, cons("JOIN", exprList, null));
		}
		
		else if (check("REMOVE")) {
		    Lexeme remove = match("REMOVE");
		    Lexeme oparen = match("OPAREN");
		    Lexeme exprList = exprList();
		    Lexeme cparen = match("CPAREN");
		    return cons("REMOVE", remove, cons("JOIN", exprList, null));
		}
		
		else if (check("SET")) {
		    Lexeme set = match("SET");
		    Lexeme oparen = match("OPAREN");
		    Lexeme exprList = exprList();
		    Lexeme cparen = match("CPAREN");
		    return cons("SET", set, cons("JOIN", exprList, null));
		}
		
		else if (check("LENGTH")) {
		    Lexeme length = match("LENGTH");
		    Lexeme oparen = match("OPAREN");
		    Lexeme exprList = exprList();
		    Lexeme cparen = match("CPAREN");
		    return cons("LENGTH", length, cons("JOIN", exprList, null));
		}
		
		else if (check("PRINT")) {
		    Lexeme print = match("PRINT");
		    Lexeme oparen = match("OPAREN");
		    Lexeme exprList = exprList();
		    Lexeme cparen = match("CPAREN");
		    return cons("PRINT", print, cons("JOIN", exprList, null));
		}
		
		else { return null; }
    }
    

    public Lexeme op(){
		if (check("EQUAL")) { return cons("EQUAL", match("EQUAL"), null); }
		
		else if (check("DEQUAL")) { return cons("DEQUAL", match("DEQUAL"), null); }
		
		else if (check("NEQUAL")) { return cons("NEQUAL", match("NEQUAL"), null); }
		
		else if (check("GREATER")) { return cons("GREATER", match("GREATER"), null); }
		
		else if (check("LESS")) { return cons("LESS", match("LESS"), null); }
		
		else if (check("GEQUAL")) { return cons("GEQUAL", match("GEQUAL"), null); }
		
		else if (check("LEQUAL")) { return cons("LEQUAL", match("LEQUAL"), null); }
		
		else if (check("PLUS")) { return cons("PLUS", match("PLUS"), null); }
		
		else if (check("MINUS")) { return cons("MINUS", match("MINUS"), null); }
		
		else if (check("MULTIPLY")) { return cons("MULTIPLY", match("MULTIPLY"), null); }
		
		else if (check("DIV")) { return cons("DIV", match("DIV"), null); }
		
		else if (check("INTDIV")) { return cons("INTDIV", match("INTDIV"), null); }
		
		else if (check("POWER")) { return cons("POWER", match("POWER"), null); }
		
		else if (check("AND")) { return cons("AND", match("AND"), null); }
		
		else if (check("OR")) { return cons("OR", match("OR"), null); }
		
		else { return null; }
    }
    
    

//----------UTIL FUNCS----------//
//DONE
	
	public Lexeme parse() {		
		Lexeme root = progDef();
		Lexeme end = match("ENDOFINPUT");
		return cons("PARSE",root,end);
	}

	public Boolean check(String type) { return curTree.type.equals(type); }
	
	public Lexeme match(String type) {
		if (check(type)) {
			return advance();
		}
		else {
			BroGramming.fatal("Syntax Expected "+type+", Received "+curTree.type+ " line: "+(curTree.lineNum-1));//change wording
			return null;
		}
	}
	
	public Lexeme advance() {
		Lexeme prev = curTree;
		curTree = curTree.left;
		prev.left = null;
		//System.out.println(prev);
		return prev;
	}
	
	public Lexeme cons(String val, Lexeme left, Lexeme right) { return new Lexeme(val, val, left, right); }
	


	





//----------PENDING FUNCS----------//


	public Boolean progDefPending() { return defPending(); }
	
	public Boolean defPending() { return varDefPending() | idDefPending() | funcDefPending(); }
	
	public Boolean varDefPending() { return check("TYPE"); }
	
	public Boolean funcDefPending() { return check("FUNC"); }
	
	public Boolean idDefPending() { return check("ID"); }
	
	public Boolean paramListPending() { return check("ID"); }
	
	public Boolean exprListPending() { return exprPending(); }
	
	public Boolean exprPending() { return unaryPending(); }
	
	public Boolean blockPending() { return check("OBRACE"); }
	
	public Boolean statementListPending() { return statementPending(); }
	
	public Boolean statementPending() { return varDefPending() |  funcDefPending() | exprPending() | whileLoopPending() | ifStatementPending() | check("RETURN"); } 
	
	public Boolean whileLoopPending() { return check("WHILE"); }
	
	public Boolean ifStatementPending() { return check("IF"); }
	
	public Boolean elseStatementPending() { return check("ELSE"); }
	
	public Boolean lambdaPending() { return check("LAMBDA"); }	
	
	public Boolean unaryPending() {
		return idDefPending() 
				| check("INTEGER")
				| check("STRING")
				| check("BOOLEAN")
				| check("NOT")
				| check("NIL")
				| check("OPAREN")
				| check("OBRACKET")
				| check("APPEND")
				| check("INSERT")
				| check("REMOVE")
				| check("SET")
				| check("BREAK")
				| check("LENGTH")
				| check("PRINT")
				| lambdaPending() 
				| funcDefPending();
		
	}
	
	public Boolean opPending() {
		
		return check("GREATER")
				| check("LESS")
				| check("EQUAL")
				| check("NEQUAL")
				| check("GEQUAL")
				| check("LEQUAL")
				| check("DEQUAL")
				| check("AND")
				| check("OR")
				| check("PLUS")
				| check("MINUS")
				| check("MULTIPLY")
				| check("DIV")
				| check("INTDIV")
				| check("POWER")
				| check("ASSIGN");
		}
}