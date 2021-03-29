// method for printing find on this website https://saksagan.ceng.metu.edu.tr/courses/ceng444/link/f3jasmintutorial.html


import ast.FLType._;

object JasminUtils {

	/**
	  * Name is written as "_Num" and this function return only the num as a string
	  */
	// val nameToAddress = (name: String) => name.drop(1)

	// def typeToString(typ: FLType): String = typ match {
	// 	case FLInt => "I"
	// 	case FLDouble => "F" 
	// 	case FLUnit => "V"
	// 	case FLFunc(args,typ) => "(" + args.map(typeToString(_)).mkString + ")" + typeToString(typ)
	// }

	// typeToString(FLFunc(Seq(FLInt,FLInt,FLDouble),FLUnit))
	
	// 	val printlnInt = """
	
	// 	.method public static printInt(I)V 
	// 		.limit locals 5 
	// 		.limit stack 5 
	// 		iload 0 
	// 		getstatic java/lang/System/out Ljava/io/PrintStream; 
	// 		swap 
	// 		invokevirtual java/io/PrintStream/println(I)V 
	// 		return 
	// 	.end method
	// 	"""
	
	// 	// Using float in order to represent double
	// 	// not really interested by precision and easier to maniuple as 32-bit like ints.
	// 	val printlnDouble = """
		
	// 	.method public static printDouble(F)V 
	// 		.limit locals 5 
	// 		.limit stack 5 
	// 		fload 0 
	// 		getstatic java/lang/System/out Ljava/io/PrintStream; 
	// 		swap 
	// 		invokevirtual java/io/PrintStream/println(F)V 
	// 		return 
	// 	.end method
	// 	"""

	// val begin: String = """
	// .class public XXXX.XXXX
	// .super java/lang/Object 
	// """ + printlnInt + printlnDouble

	// val functionHeader: (String,FLType) => String = (name,typ)  

}