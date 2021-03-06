$---------------------------------------------- array test
$-------- 3 ways to initialize
$---------same basic array behavior as java

func main(){

$--------initialize test

	print("");
	print("------initialize test-----");
	print("");

	print("array a = [1,2,3,4];");
	array a = [1,2,3,4];$<--------given initial value;
$	print("a = " + a);$-------error: cant put array in with operators
	print("a = ");
	print(a);
	

$-----------set built in test

	print("");
	print("------set test-----");
	print("");

	print("set(a,0,0);");
	set(a,0,0);
	
	print("set(a,1,10);");
	set(a,1,10);
	
	print("set(a,2,20);");
	set(a,2,20);
	
	print("set(a,3,30);");
	print("");
	
	$print("if you set out of bounds it appends to array ");
	print("");
	$print("set(a,4,40);");

	
	
	
	set(a,3,30);
	$set(a,4,40);$-------if set out of bounds it appends to arr instead of error
	print("");

	print("a = ");
	print(a);
	print("");


	print("single item array must have second arg of nothing");
	print("");
	print("Why because i said so");
	print("");
	print("ex..");
	print("");
	array a = [1,];$<------single item array must have second arg of nothing
	print("array a = [1,]");
	print("a[0]");
	print(a);

	print("");

$	print(a[1]);     $<------error: array of size 1


	array a = [];  $<--------can be empty need test insert, set, remove, on each initializer
	print("array a = [] ");
$	print("a = " + a);$-------error: cant put array in with operators

$	print(a[0]);$-------error: array is null

	print("print(a[0]) throws error");

$-------------------insert test------

	print("");
	print("------insert test-----");
	print("");

	print("insert on multi item initialize");
	print("");

	array i = [1,2,4,5,6];
	print("i = [1,2,4,5,6] ");
	print(i);
	print("");

	print("insert(array,index,value)");
	print("");

	print("insert(i,2,3)");
	print("");

	print("i  should [1,2,3,4,5,6] ");
	print(i);
	print("");

	print("insert on empty initialize");

	array i = [];
	print("");
	print("insert(i,0,1)");
	
	insert(i,0,1);

	print("insert(i,1,2)");
	print("");
	
	insert(i,1,2);

	print("i should be [1,2] ");
	print(i);




$-------------------append test------

	print("");
	print("------append test-----");
	print("");

	print("append on multi item initialize");
	print("");

	array a = [1,2,3,4,5];
	print("a = [1,2,3,4,5] ");
	print(a);
	print("");

	print("append(array,value)");
	print("");

	print("append(a,6)");
	append(a,6);
	print("");

	print("a should be [1,2,3,4,5,6] ");
	print(a);
	print("");


	print("append on empty initialize");
	print("");
	
	array a = [];
	print("array a =[]");
	print("");
	print("append(a,1)");
	
	append(a,1);

	print("append(a,2)");
	print("");
	
	append(a,2);

	print("a should be [1,2] ");
	print(a);
	print("");


$-------------------remove test------

	print("");
	print("------remove test-----");
	print("");


	array r =[1,2,3,4,5];
	print("remove on multi item initialize");
	print("");

	array r = [1,2,3,4,5];
	print("r = [1,2,3,4,5] ");
	print(r);
	print("");

	print("remove(array,index)");
	print("");

	print("remove(r,0) ");
	print("");

	remove(r,0);

	print("r  should [2,3,4,5,6] ");
	print(r);
	print("");

	print("remove on empty initialize throws error");

	array r =[];
	print("");
$	print("remove(r,0) ");
$	remove(r,0);
	print("");
	

	print("remove item from middle of array");
	print("");

	array r =[1,2,3,4,5];
	print("r = [1,2,3,4,5] ");
	print(r);
	print("");

	print("remove(r,2)");
	print("");

	remove(r,2);

	print("r should be [1,2,4,5] ");
	print(r);
	print("");







$-------------------length test------

	print("");
	print("------length test-----");
	print("");


	print("array l = [1,2,3,4,5];");
	print("");

	array l = [1,2,3,4];
	
	print(l);
	print("");
	print("length(l)");
	print(length(l));
	print("");
	
	array l =[1,];

	print("array l = [1,];");
	print("");
	print("length(l)");
	print(length(l));
	print("");


	array l =[];

	print("array l = [];");
	print("");
	print("length(l)");
	print(length(l));
	print("");



}
main()