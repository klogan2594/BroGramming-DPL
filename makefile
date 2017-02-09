run: make

arrays:
	cat TestFiles/array_test.bro

arraysx: make
	java -classpath classfiles brogramming.BroGramming TestFiles/array_test.bro

error1:
	cat TestFiles/error1.bro

error1x: make
	java -classpath classfiles brogramming.BroGramming TestFiles/error1.bro

error2:
	cat TestFiles/error2.bro

error2x: make
	java -classpath classfiles brogramming.BroGramming TestFiles/error2.bro

error3:
	cat TestFiles/error3.bro

error3x: make
	java -classpath classfiles brogramming.BroGramming TestFiles/error3.bro


conditionals:
	cat TestFiles/conditional.bro

conditionalsx: make
	java -classpath classfiles brogramming.BroGramming TestFiles/conditional.bro

recursion:
	cat TestFiles/recursion.bro

recursionx: make
	java -classpath classfiles brogramming.BroGramming TestFiles/recursion.bro

iteration:
	cat TestFiles/iteration.bro

iterationx: make
	java -classpath classfiles brogramming.BroGramming TestFiles/iteration.bro

functions:
	cat TestFiles/function.bro

functionsx: make
	java -classpath classfiles brogramming.BroGramming TestFiles/function.bro

dictionary:
	echo not implemented

dictionaryx: 
	echo not implemented

problem:
	echo not implemented

problemx: 
	echo not implemented

make:
	javac -d classfiles -sourcepath src src/brogramming/BroGramming.java


clean:
	rm -f classfiles/brogramming/*.class
