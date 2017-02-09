# BroGramming-DPL
A custom programming language based off Scheme with a Java like syntax.


Function Definition:
func *name*(*parameters*) { *body* }


Function Call in Global Environment:
*name*(*args*)


Function Call in Non Global Environment:
*name*(*args*);


Variable Definition:
//Variable definitions must be initialized.
*var type* *var name* = *var value*;
ex: int a = 10;


Conditional Definition:
if (*boolean*) { *body* }
else { *body* }


Loop Definition:
while (*boolean*) { *body* }


Array Definition/Operations:
array *arrName* = []
array *arrName* = [6,]		//Single Item arrays must have a comma, ran out of time before I could fix this bug.
array *arrName* = [3,3,5,7]

insert(*array*, *index*, *value*);	*OR*	shove(*array*, *index*, *value*);

set(*array*, *index*, *value*);	*OR*	change(*array*, *index*, *value*);

append(*array*, *value*);

remove(*array*, *index*);	*OR*	destroy(*array*, *index*);

length(*array*);	*OR*	measure(*array


Lambda Definition/Call:
func (*fname*) = lambda(*parameters*) { *body * };	//Definition
*fname*(*args*);	//Call

Operators:
+: addition, works on reals, ints, and string (as long as string is formatted as a number ("12").

-: subtraction, works on reals and ints.

/: division, works with reals and ints.

*: mutliplication, works with reals and ints.

//: integer division, works on reals and ints.

^: power, works on reals and ints.

==: equality, doesn't test for object equality, just value equality.

!=: not equality, doesn't test for object equality, just value equality.

>: greater than.

<: less than.

>=: greater than or equal to.

<=: less than or equal to.

&: logical and.

|: logical or.


Built In Functions:
print(*string*);

break;

return;