//	#!/bin/bash
//  java Brogramming.class $*

//chmod +x brogramming

//go back and fix all fatal occurences

package brogramming;

import java.io.FileNotFoundException;
import java.io.IOException;

public class BroGramming {
	
	static Lexer l;
	static Parser p;
	static Evaluator e;
	static Lexeme parsedTree;
	static Lexeme env = Evaluator.createEnv();
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		try {
			String fname = args[0];
			if (!fname.endsWith(".bro")) { fatal("Source Code must have file extension '.bro'. Exiting..."); }
			l = new Lexer(fname);
		}
		catch (ArrayIndexOutOfBoundsException indx) { fatal("No source code provided. Exiting..."); }
		catch (FileNotFoundException fnf) { fatal("File was not found. Exiting..."); }
			
		p = new Parser();
		parsedTree = p.parse();
		e = new Evaluator(env);

	}
		
	public static void fatal(String error) {
		System.out.println("ERROR: " + error);
		System.exit(0);
	}
	
	public static void fatal(String error, int lineNum) { 
		System.out.println("ERROR: " + error + " line: " + lineNum);
		System.exit(0);
	}
	
	
}
