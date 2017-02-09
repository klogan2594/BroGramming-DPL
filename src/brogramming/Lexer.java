package brogramming;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.Charset;

public class Lexer {
	static PushbackReader pbr;
	static String buffer = ""; 
	static char curChar = 0;
	static int eofInt = 0;
	static int lineNum = 1;
	static Lexeme current;	
	static Lexeme tree; //top level lexeme
	static Lexeme next;	
	static boolean real;
	
	

	//DONE
	public Lexer (String fname) throws IOException { //start makes it print
		pbr = new PushbackReader(new InputStreamReader(new FileInputStream(fname),Charset.forName("UTF-8")));
		
		while (eofInt != 65535) {
			current = lex();
			if (tree == null) {
				tree = current;
				next = tree;
			}
			else {
				next.left = current;
				next = next.left;
				if (current.type == "ENDOFINPUT") { break; }
				}			
			
		}
		//System.out.println(tree);
	}
	
	//DONE
	private static void skipWhiteSpace() throws IOException {
		curChar = (char)pbr.read();
		if (curChar == '\n') { lineNum++; }
		while (Character.isWhitespace(curChar)) {
			curChar = (char)pbr.read();
			if (curChar == '\n') { lineNum++; }
		}
		pbr.unread(curChar);
	}
	
	
	//DONE
	public static Lexeme lex() throws IOException {
		
		skipWhiteSpace();
		curChar = (char)pbr.read();
		
		if (curChar == '\uffff') {
			return new Lexeme("ENDOFINPUT","\uffff");
		}
		
		switch (curChar) {
			
			//Braces, Brackets, Parens
			case '(': return new Lexeme("OPAREN","(");
			case ')': return new Lexeme("CPAREN",")");
			case '[': return new Lexeme("OBRACKET","[");
			case ']': return new Lexeme("CBRACKET","]");
			case '{': return new Lexeme("OBRACE","{");
			case '}': return new Lexeme("CBRACE","}");
			
			//Math Functions
			case '+': return new Lexeme("PLUS","+");
			case '*': return new Lexeme("MULTIPLY","*");
			case '^': return new Lexeme("POWER","^");
			case '%': return new Lexeme("MODULO","%");
			
			//Punctuation and Logic
			case '.': return new Lexeme("PERIOD",".");
			case ',': return new Lexeme("COMMA", ",");
			case ';': return new Lexeme("SEMI",";");
			case '&': return new Lexeme("AND","&");
			case '|': return new Lexeme("OR","|");
			
			//buffers w/ Multiple Chars
			default:
				
				if (Character.isDigit(curChar)) { return lexDigit();}

				else if (Character.isLetter(curChar)) { return lexVarOrKey(); }
				
				else if (curChar == '\'') { return lexString(); } //possibly remove
				
				else if (curChar == '\"') { return lexString(); }
				
				else if (curChar == '-') { return lexSubtract(); }
				
				else if (curChar == '/') { return lexDivide(); }
				
				else if (curChar == '=' | curChar == '!' | curChar == '>' | curChar == '<' ) { return lexOp(); } 
				
				else if (curChar == '$') { 
					skipComment(); 
					return lex(); 
				}
				
				else { return new Lexeme("NO MATCH"); }
				
		}
				
	}
	
	//DONE
	private static Lexeme lexDigit() throws IOException {
		buffer = ""; 
		real = false;
		
		while (Character.isDigit(curChar)) {
			buffer += curChar;
			curChar = (char) pbr.read();
			if ( curChar == '.' ) { real  = true; buffer += curChar; curChar = (char) pbr.read(); }
		}
		
		pbr.unread(curChar);
		if (real == true) { return new Lexeme("REAL", buffer); }
		else { return new Lexeme("INTEGER", buffer); }
	}
	
	//DONE
	private static Lexeme lexString() throws IOException {
		buffer = "";
		curChar = (char) pbr.read();
		
		while (curChar != '\"' ) { buffer += curChar; curChar = (char) pbr.read(); }
		
		return new Lexeme("STRING", buffer);
	}
	
	
	//PARTIALLY DONE
	private static Lexeme lexVarOrKey() throws IOException {
		buffer = "";
		
		while (Character.isLetter(curChar) || Character.isDigit(curChar) || curChar == '.' ) { buffer += curChar; curChar = (char) pbr.read(); } 
		
		//	Keyword Defs

		pbr.unread(curChar);
		
		if (buffer.equals("if")) { return new Lexeme("IF","if"); }
		
		else if (buffer.equals("else")) { return new Lexeme("ELSE","else"); }
		
		else if (buffer.equals("boolean")) { return new Lexeme("TYPE","boolean"); }
		
		else if (buffer.equals("integer") || buffer.equals("int")) { return new Lexeme("TYPE","int"); }
		
		else if (buffer.equals("string")) { return new Lexeme("TYPE","string"); }
		
		else if (buffer.equals("real")) { return new Lexeme("TYPE","real"); }
		
		else if (buffer.equals("while")) { return new Lexeme("WHILE","while"); }
		
		else if (buffer.equals("return")) { return new Lexeme("RETURN","return"); } 
		
		else if (buffer.equals("func")) { return new Lexeme("FUNC","func"); }
		
		else if (buffer.equals("nil")) { return new Lexeme("NIL","nil"); }
		
		else if (buffer.equals("true")) { return new Lexeme("BOOLEAN","TRUE"); }
		
		else if (buffer.equals("false")) { return new Lexeme("BOOLEAN","FALSE"); }
		
		else if (buffer.equals("print") || buffer.equals("display")) { return new Lexeme("PRINT","print"); }
		
		else if (buffer.equals("array")) { return new Lexeme("TYPE","array"); }
		
		else if (buffer.equals("append")) { return new Lexeme("APPEND","append"); }
		
		else if (buffer.equals("insert") || buffer.equals("shove")) { return new Lexeme("INSERT","insert"); }
		
		else if (buffer.equals("remove") || buffer.equals("destroy")) { return new Lexeme("REMOVE","remove"); }
		
		else if (buffer.equals("set") || buffer.equals("change")) { return new Lexeme("SET","set"); }
		
		else if (buffer.equals("length") || buffer.equals("measure")) { return new Lexeme("LENGTH","length"); }
		
		else if (buffer.equals("break") || buffer.equals("stop")) { return new Lexeme("BREAK","break"); }
		
		else if (buffer.equals("lambda")) { return new Lexeme("LAMBDA","lambda"); }
		
		else { return new Lexeme("ID",buffer); }
		
	}
	
	
	//DONE
	private static Lexeme lexSubtract() throws IOException {
		real = false;
		buffer = "";
		buffer += curChar;
		curChar = (char)pbr.read();
		
		if (!(Character.isDigit(curChar))) { pbr.unread(curChar); return new Lexeme("MINUS","-"); }
		
		while (Character.isDigit(curChar)) {
			buffer += curChar;
			curChar = (char)pbr.read();
			if (curChar == '.') { real = true; buffer += curChar; curChar = (char)pbr.read(); }
		}
		
		pbr.unread(curChar);
		
		if (real == true) { return new Lexeme("REAL", buffer); }
		else { return new Lexeme("INTEGER",buffer); }
	}

	
	//DONE
	private static Lexeme lexDivide() throws IOException {
		buffer = "";
		curChar = (char)pbr.read();

		if (curChar == '/') { return new Lexeme ("INTDIV","//"); } 
		else { pbr.unread(curChar); return new Lexeme ("DIV","/"); } 
	}
	
	
	//DONE
	private static Lexeme lexOp() throws IOException {
		char prev = curChar;
		curChar = (char)pbr.read();
		
		switch (prev) {
		
		case '=': 
			if (curChar == '=') { return new Lexeme("DEQUAL", "=="); }
			else {
				pbr.unread(curChar);
				return new Lexeme ("EQUAL", "=");
			}
			
		case '!': 
			if (curChar == '=') { return new Lexeme("NEQUAL", "!="); }
			else {
				pbr.unread(curChar);
				return new Lexeme ("NOT", "!");
			}	
		
		case '<': 
				if (curChar == '=') { return new Lexeme("LEQUAL", "<="); }
				else {
					pbr.unread(curChar);
					return new Lexeme ("LESS", "<");
				}
				
		case '>': 
			if (curChar == '=') { return new Lexeme("GEQUAL", ">="); }
			else {
				pbr.unread(curChar);
				return new Lexeme ("GREATER", ">");
			}
				
		}
		
		return new Lexeme("NOT_RECOGNIZED");
		
	}



	private static void skipComment() throws IOException {
		curChar = (char)pbr.read();
		if (curChar == '\n'){
			lineNum++;
		}
		
		while (curChar != '\n') {
			curChar = (char)pbr.read();
			if (curChar == '\n'){
				lineNum++;
			}
		}
	}

}
