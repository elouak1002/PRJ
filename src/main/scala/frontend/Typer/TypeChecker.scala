package frontend.typer;

import ast.FLType._;
import ast._;

object TypeChecker {
	
	def typeEqual(type1: FLType, type2: FLType): Either[String,FLType] = {
		if (type1 == type2) Right(type1) else Left("Type error.")
	}

	def typeEqualList(argsExpr: Seq[FLType], argsType: Seq[FLType]) : Either[String, Seq[FLType]] = (argsExpr,argsType) match {
		case (type1::xs1,type2::xs2) => if (type1 == type2) typeEqualList(xs1,xs2).map{ seq => type1+:seq } else Left("Type error in function call.")
		case (Nil,Nil) => Right(argsExpr)
		case (_,_) => Left("Type error in function call.")
	}

	def operationAllowedOnType(op: String, typ: FLType) : Either[String, FLType] = (op,typ) match {
		case ("+"|"-"|"*"|"%"|"/"|"<"|"<="|"=="|"!=",FLInt|FLFloat) => Right(typ)
		case ("=="|"!=",FLBoolean) => Right(typ)
		case (_,_) => Left("Operator " + op + " not allowed on type " + typ + ".")
	}

}