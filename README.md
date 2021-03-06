# UML_Parser

A parser that takes a UML class diagram written in an expression language.

The program charges a UML class diagram from a .ucd file, offers a GUI allowing to choose the file to be charged and displays the results. 

The program also do the following functions :

1) Calculate each class's matrics.

2) Displays each matric's definition

3) Produce a .csv file containing a matrix that has all the classes found and their matrics.


The calculated matrics are :

1) ANA(c) : Average number of the arguments of local methodes of class c.
2) NOM(c) :Number of local methods / inherited from class c. Where a method is inherited and redefined locally (same name, same order and types arguments and the same type of return), it counts only once.
3) NOA(c) :Number of local/inherited attributes of class c.
4) ITC(c) : Number of times that other classes in the diagram are used as an argument type in class c's methodes.
5) ETC(c) : Number of times that c is used as argument's type in any other class's methodes.
6) CAC(c) : Number of aggregations/associations that class c participates in.
7) DIT(c) : Length of the longest path from the class c to another root class in the inheritance graph.
8) CLD(c) : Length of the longest path from the class c to another leaf class in the inheritance graph.
9) NOC(c) : Number of direct subclasses of c.
10)NOD(c) : Number of direct/indiret subclasses of c.


