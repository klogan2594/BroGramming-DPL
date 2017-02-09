package brogramming;


import static brogramming.BroGramming.fatal; //potentially just redo fatal func for this class

public class Evaluator {
	static Lexeme env;
	
	
	public Evaluator(Lexeme env) {
		env = createEnv();
		eval(env, BroGramming.parsedTree);
	}
	
	
	
	
	//EVAL FUNCS
	
	public Lexeme eval(Lexeme env, Lexeme pt) {
		
		if (pt == null) { return new Lexeme ("NIL", "NIL", null, null); } //possibly change
		
		String ptType = pt.type;
		
		//potentially add modulo
		
		switch(ptType) {
		
			case "PARSE": return evalParse(env, pt);
	
			case "PROGRAM": return evalProgam(env, pt);
	
			case "DEFINITION": return evalDef(env, pt); 
			
			case "VARDEF": return evalVarDef(env, pt);
			
			case "FDEF": return evalFuncDef(env, pt);
			
			case "FUNCCALL": return evalFuncCall(env, pt);
			
			case "IDDEF": return evalIdDef(env, pt);
			
			case "OPTPARAMLIST": return evalOptParamList(env, pt);
			
			case "PARAMLIST": return evalParamList(env, pt);
			
			case "EXPR": return evalExpr(env, pt);
			
			case "EXPRLIST": return evalExprList(env, pt);
			
			case "OPTEXPRLIST": return evalOptExprList(env, pt);
			
			case "STATEMENT": return evalStatement(env, pt);
			
			case "STATEMENTLIST": return evalStatementList(env, pt);
			
			case "OPTSTATEMENTLIST": return evalOptStatementList(env, pt);
			
			case "WHILELOOP": return evalWhileLoop(env, pt);
			
			case "IFSTATEMENT": return evalIfStatement(env, pt);
			
			case "ELSESTATEMENT": return evalElseStatement(env, pt);
			
			case "OPTELSESTATEMENT": return evalOptElseStatement(env, pt);
			
			case "LAMBDA": return evalLambda(env, pt);
			
			case "BLOCK": return evalBlock(env, pt);
			
			case "UNARY": return evalUnary(env, pt);
			
			case "APPEND": return evalAppend(env, pt);
			
			case "INSERT": return evalInsert(env, pt);
			
			case "BREAK": return evalBreak(env, pt);
			
			case "ARRAY": return evalArray(env, pt);			
			
			case "INDEXARRAY": return evalIndexArray(env, pt); 
			
			case "REMOVE": return evalDelete(env, pt);
			
			case "SET": return evalSet(env, pt);
			
			case "LENGTH": return evalLength(env, pt);
			
			
			case "PRINT": return evalPrint(env, pt);
			
			case "INTEGER": return evalInt(env, pt);
			
			case "REAL": return evalReal(env, pt);
			
			case "STRING": return evalString(env, pt);
			
			case "BOOLEAN": return evalBoolean(env, pt);
			
			case "NIL": return evalNil(env, pt);
			
			case "ID": return evalId(env, pt);
			
			case "RETURN": return evalReturn(env, pt);
			
			
			
			case "AND": return evalAnd(env, pt);
			
			case "OR": return evalOr(env, pt);
			
			case "NOT": return evalUnary(env, pt);
			
			case "PLUS": return evalPlus(env, pt);
			
			case "MINUS": return evalMinus(env, pt);
			
			case "MULTIPLY": return evalMultiply(env, pt);
			
			case "DIV": return evalDivide(env, pt);
			
			case "INTDIV": return evalIntDivide(env, pt);
			
			case "POWER": return evalPower(env, pt);
			
			case "EQUAL": return evalEqual(env, pt);
			
			case "DEQUAL": return evalDoubleEqual(env, pt);
			
			case "NEQUAL": return evalNotEqual(env, pt); 
			
			case "GREATER": return evalGreater(env, pt);
			
			case "LESS": return evalLess(env, pt);
			
			case "GEQUAL": return evalGreaterEqual(env, pt);
			
			case "LEQUAL": return evalLessEqual(env, pt);
			
			default: BroGramming.fatal(pt.type+" : "+pt.val+"line#: "+pt.lineNum); 
					 return null;
		
		}
	}
	
	
	//EVAL FUNCTIONS BELOW

	
	//DONE
	private Lexeme evalFuncCall(Lexeme env, Lexeme pt) {
		int i, j; 
		
		Lexeme args = getArgs(pt);
		Lexeme fName = pt.left;
		Lexeme closure = eval(env, fName);
		
		if (closure == null) { fatal("Closure was null.", fName.lineNum); }
		else if ( closure.type != "CLOSURE") { fatal("Attempted to call closure: " + closure.val + " as a function.", fName.lineNum); }
		
		Lexeme defEnv = closure.right.right;
		Lexeme body = closure.right.left;
		Lexeme params = closure.left;
		
		if (args.left != null) { args = args.left; }
		Lexeme envArgs = makeArgs(args, env);
		
	
		if (envArgs != null && params != null) { //dont rea`lly need if block
			j = params.size();
			i = envArgs.size();
		}
		
		if ((args.left == null && params != null) || (args.left != null && params == null)) { 
			fatal("Incorrect number of arguments to function: " + fName, fName.lineNum);
		}
		
		if((envArgs != null && params != null) && (envArgs.size() != params.size())) {
			fatal("Incorrect number of arguments to function: " + fName, fName.lineNum);
		}
		
		Lexeme xenv = extendEnv(params, envArgs, defEnv);
		return eval(xenv, body);
	}


	
	//DONE
	private Lexeme evalFuncDef(Lexeme env, Lexeme pt) {
		if (pt.right.right.right.right.left != null && pt.right.right.right.right.left.type.equals("SEMI")) {
			Lexeme var = pt.right.left;
			Lexeme closure = eval(env, pt.right.right.right.left );
			return insert(var, closure, env);
		}
		
		Lexeme var = pt.right.left;
		Lexeme params = pt.right.right.right.left.left;
		Lexeme body = pt.right.right.right.right.right.left;
		Lexeme rightOfClosure = cons("JOIN", body, env);
		Lexeme closure = cons("CLOSURE", params, rightOfClosure);
		return insert(var, closure, env);
	}




	
	private Lexeme evalLessEqual(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		
		
		int i = lVal.val.compareTo(rVal.val);
		String v = (i <= 0) ? "TRUE" : "FALSE";
		return new Lexeme("BOOLEAN", v);
		
	}




	private Lexeme evalGreaterEqual(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		
		int i = lVal.val.compareTo(rVal.val);
		String v = (i >= 0) ? "TRUE" : "FALSE";
		return new Lexeme("BOOLEAN", v);
	}




	private Lexeme evalLess(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		
		int i = lVal.val.compareTo(rVal.val);
		String v = (i < 0) ? "TRUE" : "FALSE";
		return new Lexeme("BOOLEAN", v);
	}




	private Lexeme evalGreater(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		
		int i = lVal.val.compareTo(rVal.val);
		String v = (i > 0) ? "TRUE" : "FALSE";
		return new Lexeme("BOOLEAN", v);
	}




	private Lexeme evalNotEqual(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		
		int i = lVal.val.compareTo(rVal.val);
		String v = (i != 0) ? "TRUE" : "FALSE";
		return new Lexeme("BOOLEAN", v);
	}

	

	//string true could equal bool true
	private Lexeme evalDoubleEqual(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		int i = lVal.val.compareTo(rVal.val);
		String v = (i == 0) ? "TRUE" : "FALSE";
		return new Lexeme("BOOLEAN", v);
	}



	//DONE
	//EVAL ASSIGN
	private Lexeme evalEqual(Lexeme env, Lexeme pt) {
		Lexeme var = pt.left.left.left;
		Lexeme val = eval(env, pt.right.left);
		return update(env, var.val, val, var.lineNum);
	}



	//might should add try block to catch non numbers
	private Lexeme evalPower(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		double result;
		if ( (rVal.type.equals("INTEGER") || rVal.type.equals("REAL"))
				&& (lVal.type.equals("INTEGER") || lVal.type.equals("REAL")) ) {
			result = Math.pow(Float.parseFloat(lVal.val), Float.parseFloat(rVal.val));
			if (result == (int)result) { return new Lexeme("INTEGER",Integer.toString((int)result)); }
			else { return new Lexeme("REAL", Double.toString(result)); }
		}
		else { 
			BroGramming.fatal("Cannot raise type: " + lVal.type + " to type: " + rVal.type + " power.", rVal.lineNum);
			return null;
		}
	}



	//DONE
	private Lexeme evalIntDivide(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		int result;
		if ( (rVal.type.equals("INTEGER") || rVal.type.equals("REAL"))
				&& (lVal.type.equals("INTEGER") || lVal.type.equals("REAL")) ) {
			result = (int)Math.floor((Double.parseDouble(lVal.val) / Double.parseDouble(rVal.val)));
			return new Lexeme("INTEGER", Integer.toString(result)); 
		}
		else { 
			BroGramming.fatal("Cannot integer divide type: " + lVal.type + " by type: " + rVal.type, lVal.lineNum);
			return null;
		}
	}



	//DONE
	private Lexeme evalDivide(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		double result;
		if ( (rVal.type.equals("INTEGER") || rVal.type.equals("REAL"))
				&& (lVal.type.equals("INTEGER") || lVal.type.equals("REAL")) ) {
			result = (Double.parseDouble(lVal.val) / Double.parseDouble(rVal.val));
			if (result == (int)result) { return new Lexeme("INTEGER",Integer.toString((int)result)); }
			else { return new Lexeme("REAL", Double.toString(result)); }
		}
		else { 
			BroGramming.fatal("Cannot divide type: " + lVal.type + " by type: " + rVal.type, lVal.lineNum);
			return null;
		}
	}



	//DONE
	private Lexeme evalMultiply(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		double result;
		if ( (rVal.type.equals("INTEGER") || rVal.type.equals("REAL"))
				&& (lVal.type.equals("INTEGER") || lVal.type.equals("REAL")) ) {
			result = (Double.parseDouble(lVal.val) * Double.parseDouble(rVal.val));
			if (result == (int)result) { return new Lexeme("INTEGER",Integer.toString((int)result)); }
			else { return new Lexeme("REAL", Double.toString(result)); }
		}
		else { 
			BroGramming.fatal("Cannot multiply type: " + lVal.type + " by type: " + rVal.type, lVal.lineNum);
			return null;
		}
	}



	//potentially make into pseudo substring function
	//DONE
	private Lexeme evalMinus(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		double result;
		if ( (lVal.type.equals("INTEGER") || lVal.type.equals("REAL"))
				&& (rVal.type.equals("INTEGER") || rVal.type.equals("REAL")) ) {
			result = (Double.parseDouble(lVal.val) - Double.parseDouble(rVal.val));
			if (result == (int)result) { return new Lexeme("INTEGER",Integer.toString((int)result)); }
			else { return new Lexeme("REAL", Double.toString(result)); }
		}
		else { 
			BroGramming.fatal("Cannot subtract type: " + lVal.type + " to type: " + rVal.type, lVal.lineNum);
			return null;
		}
	}



	//DONE
	//Doesn't handle REAL/STRING ops
	private Lexeme evalPlus(Lexeme env, Lexeme pt) {
		Lexeme rVal = eval(env, pt.right);
		Lexeme lVal = eval(env, pt.left);
		double result;
		if ( (rVal.type.equals("INTEGER") || rVal.type.equals("REAL"))
				&& (lVal.type.equals("INTEGER") || lVal.type.equals("REAL")) ) {
			result = (Double.parseDouble(lVal.val) + Double.parseDouble(rVal.val));
			if (result == (int)result) { return new Lexeme("INTEGER",Integer.toString((int)result)); }
			else { return new Lexeme("FLOAT", Double.toString(result)); }
		}
		else if (lVal.type.equals("STRING") && rVal.type.equals("STRING")) {
			return new Lexeme("STRING", (lVal.val + rVal.val));
		}
		
		else if (lVal.type.equals("INTEGER") && rVal.type.equals("STRING")) {
			try {
				int sum = (Integer.parseInt(rVal.val) + Integer.parseInt(lVal.val));
				return new Lexeme("INTEGER", Integer.toString(sum));
			}
			catch (NumberFormatException nfe) { 
				fatal(rVal.val + " is not a correctly formed number string.", lVal.lineNum);
				return null;
			}
		}
		
		else if (lVal.type.equals("STRING") && rVal.type.equals("INTEGER")) {
			return new Lexeme("STRING", (rVal.val + lVal.val));
		}
		
		else { 
			BroGramming.fatal("Cannot add type: " + lVal.type + " to type: " + rVal.type);
			return null;
		}
	}




	//DONE
	private Lexeme evalOr(Lexeme env, Lexeme pt) {
		Lexeme left = eval(env, pt.left);
		Lexeme right = eval(env, pt.right);
		
		if ( (left.type.equals("INTEGER") || left.type.equals("REAL"))
				&& (right.type.equals("INTEGER") || right.type.equals("REAL"))) {
			return new Lexeme("INTEGER", Integer.toString(Integer.parseInt(left.val) | Integer.parseInt(right.val)));
		}
		
		else if (left.type.equals("BOOLEAN") && right.type.equals("BOOLEAN")) { 
			return new Lexeme("BOOLEAN", Boolean.toString(Boolean.parseBoolean(left.val) | Boolean.parseBoolean(right.val)));
		}
		
		else { 
			fatal("Can't OR: " + left.type + " and " + right.type, left.lineNum);
			return null;
		}
	}



	//DONE
	private Lexeme evalAnd(Lexeme env, Lexeme pt) {
		Lexeme left = eval(env, pt.left);
		Lexeme right = eval(env, pt.right);
		
		if ( (left.type.equals("INTEGER") || left.type.equals("REAL"))
				&& (right.type.equals("INTEGER") || right.type.equals("REAL"))) {
			return new Lexeme("INTEGER", Integer.toString(Integer.parseInt(left.val) & Integer.parseInt(right.val)));
		}
		
		else if (left.type.equals("BOOLEAN") && right.type.equals("BOOLEAN")) { 
			return new Lexeme("BOOLEAN", Boolean.toString(Boolean.parseBoolean(left.val) & Boolean.parseBoolean(right.val)));
		}
		
		else { 
			fatal("Can't AND: " + left.type + " and " + right.type, left.lineNum);
			return null;
		}
	}



	//DONE
	private Lexeme evalReturn(Lexeme env, Lexeme pt) {
		return pt;
	}



	//DONE
	private Lexeme evalId(Lexeme env, Lexeme pt) {
		return lookup(env, pt.val, pt.lineNum);
	}



	//DONE
	private Lexeme evalNil(Lexeme env, Lexeme pt) {
		return pt;
	}



	//DONE
	private Lexeme evalBoolean(Lexeme env, Lexeme pt) {
		return pt;
	}



	//DONE
	private Lexeme evalString(Lexeme env, Lexeme pt) {
		return pt;
	}



	//DONE
	private Lexeme evalReal(Lexeme env, Lexeme pt) {
		return pt;
	}



	//DONE
	private Lexeme evalInt(Lexeme env, Lexeme pt) {
		return pt;
	}



	//DONE
	private Lexeme evalLength(Lexeme env, Lexeme pt) {
		Lexeme envArgs = eval(env, pt.right.left);
		return new Lexeme("INTEGER", Integer.toString(envArgs.arrayMems.size()));
	}



	//DONE
	private Lexeme evalSet(Lexeme env, Lexeme pt) {
		Lexeme envArgs = eval(env, pt.right.left);
		Lexeme array = envArgs.left;
		Lexeme index = envArgs.right.left;
		Lexeme val = envArgs.right.right;
		int ndex = Integer.parseInt((eval(env, index)).val);
		
		try { 
			return array.arrayMems.set(ndex, val); 
		}
		catch (IndexOutOfBoundsException ie) { 
			BroGramming.fatal("Index out of bounds while trying to set.", envArgs.right.lineNum); 
			return null;
		}
		
	}



	//DONE
	private Lexeme evalDelete(Lexeme env, Lexeme pt) {
		Lexeme envArgs = eval(env, pt.right.left);
		Lexeme array = envArgs.left;
		int indexVal = Integer.parseInt((eval(env, envArgs.right)).val);
		
		try {
			return array.arrayMems.remove(indexVal);
		}
		catch (IndexOutOfBoundsException indx) { 
			BroGramming.fatal("Index out of bounds while trying to remove.", envArgs.right.lineNum);
			return null;
		}
	}


  
	//DONE
	private Lexeme evalIndexArray(Lexeme env, Lexeme pt) {
		Lexeme array = lookup(env, pt.left.val, pt.left.lineNum);
		int index = Integer.parseInt(eval(env, pt.right.right.left).val);
		try {
			return array.arrayMems.get(index);
		}
		catch (IndexOutOfBoundsException idk) {
			BroGramming.fatal("Attempted to access array at index: " + index + ", array size: " + array.arrayMems.size(), eval(env, pt.right.right.left).lineNum );
			return null;
		}
	}



	//DONE
	private Lexeme evalArray(Lexeme env, Lexeme pt) {
		return pt;
	}



	//DONE
	private Lexeme evalBreak(Lexeme env, Lexeme pt) {
		return pt;
	}



	//DONE
	private Lexeme evalInsert(Lexeme env, Lexeme pt) {
		Lexeme envArgs = eval(env, pt.right.left);
		Lexeme array = envArgs.left;
		Lexeme index = envArgs.right.left;
		Lexeme val = envArgs.right.right;
		int indexVal = Integer.parseInt((eval(env, index)).val);
		try {
			array.arrayMems.add(indexVal, val);
			return val;
		}
		catch (IndexOutOfBoundsException idk) {
			BroGramming.fatal("Attempted to insert element into array at index: " + index + ", array size: " + array.arrayMems.size(), envArgs.right.lineNum );
			return null;
		}
	}



	//DONE
	private Lexeme evalAppend(Lexeme env, Lexeme pt) {
		Lexeme envArgs = eval(env, pt.right.left);
		Lexeme array = envArgs.left;
		Lexeme val = envArgs.right;
		array.arrayMems.add(val);
		return val;
	}



	//DONE
	private Lexeme evalUnary(Lexeme env, Lexeme pt) {
		Lexeme elements = null;
		
		if (pt.right == null) { return eval(env, pt.left); }
		
		else if (pt.left.type.equals("OPAREN")) { return eval(env, pt.right.left); }
		
		else if (pt.left.type.equals("OBRACKET")) { elements = eval(env, pt.right.left); }
		
		else if (pt.left.type.equals("NOT")) { 
			
			Lexeme temp =  eval(env, pt.right.left); 
			if (temp.type.equals("BOOLEAN")) {
				temp.val = Boolean.toString(!Boolean.parseBoolean(temp.val));
				return temp;
			}
			
			else { fatal("Can't not type: " + temp.type, temp.lineNum); }
		} 
		
		return eval(env, new Lexeme("ARRAY", "ARRAY", elements, null)); 
	}



	//DONE
	private Lexeme evalBlock(Lexeme env, Lexeme pt) {
		return eval(env, pt.right.left);
	}



	//DONE
	private Lexeme evalLambda(Lexeme env, Lexeme pt) {
		return new Lexeme("CLOSURE", "CLOSURE", eval(env, pt.right.right.left), (new Lexeme("JOIN", "JOIN", pt.right.right.right.right.left, env)));
	}



	//DONE
	private Lexeme evalOptElseStatement(Lexeme env, Lexeme pt) {
		if (pt.left != null) { return eval(env, pt.left); }
		else { return null; }
	}



	//DONE
	private Lexeme evalElseStatement(Lexeme env, Lexeme pt) {
		return eval(env, pt.right.left);
	}



	//DONE
	private Lexeme evalIfStatement(Lexeme env, Lexeme pt) {
		if ((eval(env, pt.right.right.left)).val.equals("TRUE")) { return eval(env, pt.right.right.right.right.left); }
		else { return eval(env, pt.right.right.right.right.right.left); }
	}



	//DONE
	private Lexeme evalWhileLoop(Lexeme env, Lexeme pt) {
		Lexeme tmp = null;
		while ((eval(env, pt.right.right.left).val.equals("TRUE"))) {
			tmp = eval(env, pt.right.right.right.right.left);
			if (tmp.type.equals("BREAK")) { return tmp.right; }
		}
		return tmp;
	}



	//DONE
	private Lexeme evalOptStatementList(Lexeme env, Lexeme pt) {
		if (pt.left != null) { return eval(env, pt.left); }
		else { return null; }
	}



	//DONE
	private Lexeme evalStatementList(Lexeme env, Lexeme pt) {

		Lexeme prev = null;
		Lexeme end = null;
		
		while (pt != null) {
			prev = end;
			end = eval(env, pt.left);
			if (end != null) {
				if (end.type.equals("RETURNED")) { end = end.left; break; }
				
				if (end.left != null && end.left.type.equals("RETURN")) { end = cons("RETURNED", eval(env, end.right.left), null); break; }
				
				if (end.type.equals("BREAK")) {
					if (end.right == null) { end.right = prev; }
					break;
				}
			}
				
			if (pt.right != null) { pt = pt.right.left; }
			else { break; }
		}
		return end;
	}



	//DONE
	private Lexeme evalStatement(Lexeme env, Lexeme pt) {
		if (pt.right == null || pt.left.type.equals("EXPR") || pt.left.type.equals("PRINT")) { return eval(env,  pt.left); }
		else if (pt.left.type.equals("RETURN")) { return pt; }
		else { BroGramming.fatal("Misformed statement. ", pt.lineNum); }
		return null;
	}



	//DONE
	private Lexeme evalOptExprList(Lexeme env, Lexeme pt) {
		if (pt.left != null) { return eval(env, pt.left); }
		else { return null; }
	}



	//DONE
	private Lexeme evalExprList(Lexeme env, Lexeme pt) {
		Lexeme nw = null;
		if (pt.right == null) { return eval(env, pt.left); }
		if (pt.right.left != null) { nw = new Lexeme("JOIN", "JOIN", eval(env, pt.left), eval(env, pt.right)); }
		return nw;
	}



	//DONE
	private Lexeme evalExpr(Lexeme env, Lexeme pt) {
		return eval(env, pt.left);
	}



	//DONE
	private Lexeme evalParamList(Lexeme env, Lexeme pt) {
		Lexeme nw = null;
		if (pt.right == null) { return pt.left; }
		if (pt.right.left != null) { nw = new Lexeme("JOIN", "JOIN", pt.left, cons("JOIN", eval(env, pt.right), null)); }
		return nw;
	}



	//DONE
	private Lexeme evalOptParamList(Lexeme env, Lexeme pt) {
		if (pt.left != null) { return eval(env, pt.left); }
		else { return null; }
	}



	//DONE
	private Lexeme evalIdDef(Lexeme env, Lexeme pt) {
		return eval(env, pt.left);
	}



	//DONE
	private Lexeme evalVarDef(Lexeme env, Lexeme pt) {
		Lexeme var = pt.right.left;
		Lexeme val = eval(env, pt.right.right.right.left);
		if (val.type.equals("ARRAY")) {
			Lexeme tmp = val.left;
			while (tmp != null && tmp.type.equals("JOIN")) {
				val.arrayMems.add(tmp.left);
				tmp = tmp.right;
			}
			val.arrayMems.add(tmp);
			val.left = null;
		}
		return insert(var, val, env);		
	}



	//DONE
	private Lexeme evalDef(Lexeme env, Lexeme pt) {
		return eval(env, pt.left);
	}



	//NOT COMPLETE
	private Lexeme evalProgam(Lexeme env, Lexeme pt) {
		while (pt.right != null) {
			eval(env, pt.left);
			pt = pt.right.left; 
		}
		return eval(env, pt.left);
	}



	//DONE
	private Lexeme evalPrint(Lexeme env, Lexeme pt) {
		Lexeme envArgs;
		
		if (pt.right != null) { envArgs = eval(env, pt.right.left); }
		else { envArgs = eval(env, pt); }

		try {
			if (envArgs.type.equals("JOIN")) {
				System.out.print(envArgs.left.val + "\n");
				return envArgs;
			}
			
			if (envArgs.type.equals("ARRAY")) { 
				System.out.print("[");
				for (Lexeme lx : envArgs.arrayMems) { 
					if ((lx != null) && lx.type.equals("NIL")) { continue; }
					if ((lx != null) && !lx.val.equals("ARRAY")) { 
						//if (envArgs.arrayMems.size()-1 == getIndex(lx))						//don't print comma
						System.out.print(lx.val + ", "); 
						}
					else if (lx != null) { evalPrint(env, lx, false); }
				}
				System.out.print("] \n");
			}
			else {
				System.out.println(envArgs.val + "\n");
			}
		}
		catch (NullPointerException npe) { BroGramming.fatal("Null Pointer Exception when calling print.", pt.lineNum); }
		return envArgs; 
	}
	
	
	//Recursive Printer Helper For Nested Arrays
	private Lexeme evalPrint(Lexeme env, Lexeme pt, boolean newLine) {
		Lexeme envArgs;
		
		if (pt.right != null) { envArgs = eval(env, pt.right.left); }
		else { envArgs = eval(env, pt); }

		try {
			if (envArgs.type.equals("JOIN")) {
				System.out.print(envArgs.left.val + "\n");
				return envArgs;
			}
			
			if (envArgs.type.equals("ARRAY")) { 
				System.out.print("[");
				for (Lexeme lx : envArgs.arrayMems) { 
					if ((lx != null) && lx.type.equals("NIL")) { continue; }
					if ((lx != null) && !lx.val.equals("ARRAY")) { 
						//if (envArgs.arrayMems.size()-1 == getIndex(lx))						//don't print comma
						System.out.print(lx.val + ", "); 
						}
					else if (lx != null) { evalPrint(env, lx, false); }
				}
				System.out.print("]");
			}
			else {
				System.out.println(envArgs.val + "\n");
			}
		}
		catch (NullPointerException npe) { BroGramming.fatal("Null Pointer Exception when calling print.", pt.lineNum); }
		return envArgs;
	}
	
	
	//DONE
    private Lexeme evalParse(Lexeme env, Lexeme pt) { return eval(env, pt.left); }




	public static Lexeme cons(String type, Lexeme l, Lexeme r) { return new Lexeme(type, type, l, r); }
    
    public Lexeme car(Lexeme lx) { return lx.left; }
    
    public Lexeme cdr(Lexeme lx) { return lx.right; }
    
    public void setcar(Lexeme lx, Lexeme l) { lx.left=l; }
    
    public void setcdr(Lexeme lx, Lexeme r) { lx.right=r; }
    
    public String type(Lexeme lx) { return lx.type; }
	
    public static Lexeme createEnv() { return extendEnv(null, null, null); }
    
    public static Lexeme extendEnv( Lexeme vars, Lexeme vals, Lexeme env) { return cons("ENV", makeTable(vars, vals), env); }
    
    public static Lexeme makeTable(Lexeme vars, Lexeme vals) { return cons("TABLE", vars, vals); }
    
    public Lexeme lookup(Lexeme env, String var, int lineNum) {
    	while (env != null) {
    		Lexeme vars = car(car(env));
    		Lexeme vals = cdr(car(env));
    		while (vars != null) {
    			if (vars.left != null && var.equals(car(vars).val)) { 
    				return car(vals); }
    			vars = cdr(vars);
    			vals = cdr(vals);
    		}
    		env = cdr(env);	
    	}
    	BroGramming.fatal("Variable "+ var + " is undefined.", lineNum ); 
    	return null;	
    }
    
    public Lexeme update(Lexeme env, String var, Lexeme val, int lineNum) {
    	while (env != null) {
    		Lexeme vars = car(car(env));
    		Lexeme vals = cdr(car(env));
    		while (vars != null) {
    			if (var.equals(car(vars).val)) {
    				setcar(vars, new Lexeme(var, var, null, null));
    				setcar(vals, new Lexeme(val.type, val.val, null, null));
    				return car(vals);
    			}
    			else {
    				vars = cdr(vars);
    				vals = cdr(vals);
    			}
        	}
    		env = cdr(env);
    	}
    	BroGramming.fatal("Variable "+ var + " is undefined.", lineNum); 
    	return null;	
    }
	
    public Lexeme createArray(Lexeme elements) {
    	Lexeme curElement = elements.left;
    	if (curElement == null) { return elements; } 
    	else {
    		while (curElement.left.type.equals("JOIN")) {
    			elements.arrayMems.add(curElement.left);
    			curElement = curElement.right;
    			if (!curElement.type.equals("JOIN") && curElement.type != null) { 
    				elements.arrayMems.add(curElement);
    			}
    		}
    	}
    	return elements;
    }
    

    public Lexeme insert(Lexeme var, Lexeme val, Lexeme env) {
    	setcar(car(env), cons("JOIN", var, car(car(env))));
    	setcdr(car(env), cons("JOIN", val, cdr(car(env))));
    	return val;
    }
     
	public void printLocalEnv(Lexeme env) {
		Lexeme vars = car(car(env));
		Lexeme vals = cdr(car(env));
		while (vars != null) {
			System.out.println(car(vars).val + " : " + car(vals).val);
			vars = cdr(vars);
			vals = cdr(vals);
		}
	}
	
	public void printEnv(Lexeme env) {
		while (env != null) {
			Lexeme vars = car(car(env));
			Lexeme vals = cdr(car(env));
			System.out.println("Displaying environment(s): ");
			while (vars != null) {
				System.out.println(car(vars).val + " : " + car(vals).val);
				vars = cdr(vars);
				vals = cdr(vals);
			}
			System.out.println("-----------------------------------------------");
			env = cdr(env);
		}
	}
	
	
	public Lexeme getArgs(Lexeme lx) {
		if (lx.right == null) { return null; }
		else { return lx.right.right.left; }
	}
	

	private Lexeme makeArgs(Lexeme pt, Lexeme env) {
		Lexeme right = null;
		Lexeme nw = null;
		
		if (pt.right == null) { 
			return cons("JOIN", eval(env, pt.left), null);
		}
		
		if (pt.right.left != null) { 
			right = makeArgs(pt.right, env);
			nw = new Lexeme("JOIN", "JOIN", eval(env, pt.left), right);
		}
		
		return nw;
	}

	
}
