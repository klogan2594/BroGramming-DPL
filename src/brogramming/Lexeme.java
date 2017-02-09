package brogramming;

import java.lang.reflect.Type;
import java.util.ArrayList;

class Lexeme implements Type {
	
	String type = null;
	String val = null; //val
	int integer = 0;
	Lexeme right = null;
	Lexeme left = null;
	int lineNum = 0;
	ArrayList<Lexeme> arrayMems;
	
	public Lexeme (String type) {
		this.type = type;
		this.lineNum = Lexer.lineNum;
		if (this.type == "ARRAY") {
			arrayMems = new ArrayList(); // could potentially need to change to "new ArrayList<Lexeme>();"
		}
	}

	
	public Lexeme (String type, String val) {
		this.type = type;
		this.val = val;
		this.lineNum = Lexer.lineNum;
		if (this.type == "ARRAY") {
			arrayMems = new ArrayList(); // could potentially need to change to "new ArrayList<Lexeme>();"
		}
	}	
	
	
	public Lexeme (String type, String val, Lexeme left, Lexeme right) {
		this.type = type;
		this.right = right;
		this.left = left;
		this.val = val;
		this.lineNum = Lexer.lineNum;
		if (this.type == "ARRAY") {
			arrayMems = new ArrayList(); // could potentially need to change to "new ArrayList<Lexeme>();"
		}
	}
	
	public int size() { int i =1; return size(i); }
	
	public int size(int i) { if (right == null) { return i; } return right.size(i+1); };
	
	
	
	@Override
	public String toString() {
		String s = "";
		if (type != null) { s += type + " ";  }
		if (val != null) { s += val + " " + "line #: " + lineNum; }
		if (left != null) { s += "\nLeft: " + left.toString(); }
		if (right != null) { s+= "\nRight: " + right.toString(); }
		return s;
				
		}
	}

	


	
