package frontend.typer;

import frontend.struct._;
import frontend.struct.SymbolTable._;
import frontend.struct.Sym._;

import frontend.typer.TypeChecker._;

import ast.TypeAst._;
import ast.Ast._;
import ast.FLType._;
import ast._;

object ExprTyper {

	def typeVal(valExpr: Ast.Expr.Val, symT: SymbolTable) : Either[String, (TypeAst.TypeExpr.TyVal,SymbolTable)] = valExpr match {
		case Ast.Expr.Val(name, typ, expr) => for {
			tyExpr <- typeExpr(expr,symT)
			valType <- typeEqual(createSingleType(typ),getNodeType(tyExpr))
			newSymT <- SymbolTable.putSymbol(name, ValueSym, valType, symT)
		} yield (TypeAst.TypeExpr.TyVal(name,tyExpr,valType,FLUnit),newSymT)
	}

	def typeIf(ifExpr: Ast.Expr.If, symT: SymbolTable): Either[String, TypeAst.TypeExpr.TyIf] = ifExpr match {
		case Ast.Expr.If(bexp,block1,block2) => for {
			tyBexp <- typeBexp(bexp, symT) // typed bexp
			tyBlock1 <- typeBlock(block1, openScope(symT)) // typed first block
			tyBlock2 <- typeBlock(block2, openScope(symT)) // typed second block
			block1Type = getBlockType(tyBlock1) // type of block1 is the type of the last expression
			block2Type = getBlockType(tyBlock2) // type of block1 is the type of the last expression
			ifType <- typeEqual(block1Type,block2Type) // type of if expr
		} yield (TypeAst.TypeExpr.TyIf(tyBexp,tyBlock1,tyBlock2,ifType)) // typed if
	}

	def typeAssign(assignExpr: Ast.Expr.Assign, symT: SymbolTable): Either[String, TypeAst.TypeExpr.TyAssign] = assignExpr match {
		case Ast.Expr.Assign(name,args) => for {
			funcSym <- SymbolTable.getSymbolOfCat(name,FunctionSym(args.length), symT) // type of function symbol with given name.
			tyArgs <- typeBlock(args, symT) // type the args (args or not really a block but still a list of expr).
			exprTypes = tyArgs.map(tyExpr => getNodeType(tyExpr))
			argsTypes = funcSym.symTyp match { case FLFunc(args,typ) => args }
			_ <- typeEqualList(exprTypes,argsTypes) // check the type of each argument.
			returnType = funcSym.symTyp match { case FLFunc(args,typ) => typ }
		} yield (TypeAst.TypeExpr.TyAssign(name,tyArgs,returnType))
	}

	def typeValue(valueExpr: Ast.Expr.Value, symT: SymbolTable): Either[String, TypeAst.TypeExpr.TyValue] = valueExpr match {
		case Ast.Expr.Value(name) => for {
			valType <- SymbolTable.getSymbolOfCat(name,ValueSym,symT) // check if the name has already been defined
		} yield(TypeAst.TypeExpr.TyValue(name,valType.symTyp))
	}

	def typeAop(aopExpr: Ast.Expr.Aop, symT: SymbolTable): Either[String, TypeAst.TypeExpr.TyAop] = aopExpr match {
		case Ast.Expr.Aop(op,aexp1,aexp2) => for {
			tyAexp1 <- typeExpr(aexp1, symT) 
			tyAexp2 <- typeExpr(aexp2, symT)
			aopType <- typeEqual(getNodeType(tyAexp1),getNodeType(tyAexp2))
			opType <- TypeChecker.operationAllowedOnType(op, aopType)
		} yield (TypeAst.TypeExpr.TyAop(op,tyAexp1,tyAexp2,opType))
	}

	def typeWrite(writeExpr: Ast.Expr.Write, symT: SymbolTable): Either[String, TypeAst.TypeExpr.TyWrite] = writeExpr match {
		case Ast.Expr.Write(expr) => for {
			tyExpr <- typeExpr(expr, symT) // type the expression.
			_ <- if ((getNodeType(tyExpr) == FLInt) || (getNodeType(tyExpr) == FLDouble)) Right(tyExpr) else Left("Can not write a value of type " + getNodeType(tyExpr)) 
		} yield (TypeAst.TypeExpr.TyWrite(tyExpr,FLUnit)) // Writing is a unit operation.
	}

	def typeExpr(expr: Ast.Expr, symT: SymbolTable) : Either[String, TypeAst.TypeExpr] = expr match {
		case expr: Ast.Expr.If => typeIf(expr,symT)
		case expr: Ast.Expr.Assign => typeAssign(expr,symT)
		case expr: Ast.Expr.Write => typeWrite(expr,symT)
		case expr: Ast.Expr.Aop => typeAop(expr,symT)
		case expr: Ast.Expr.Value => typeValue(expr,symT)
		case expr: Ast.Expr.Bexp.Bop => typeBexp(expr,symT)
		case Ast.Expr.IntExpr(num) => Right(TypeAst.TypeExpr.TyIntExpr(num,FLInt)) 
		case Ast.Expr.DoubleExpr(num) => Right(TypeAst.TypeExpr.TyDoubleExpr(num,FLDouble))
		case Ast.Expr.BooleanExpr(bool) => Right(TypeAst.TypeExpr.TyBooleanExpr(bool,FLBoolean))
		case _ => Left("Syntax error.")
	}

	def typeBexp(bexp: Ast.Expr.Bexp, symT: SymbolTable) : Either[String, TypeAst.TypeExpr.TypeBexp.TyBop] = bexp match {
		case Ast.Expr.Bexp.Bop(op,expr1,expr2) => for {
			tyExpr1 <- typeExpr(expr1,symT)
			tyExpr2 <- typeExpr(expr2,symT)
			bexpType <- typeEqual(getNodeType(tyExpr1),getNodeType(tyExpr2))
			opType <- TypeChecker.operationAllowedOnType(op, bexpType)
		} yield (TypeAst.TypeExpr.TypeBexp.TyBop(op,tyExpr1,tyExpr2,bexpType,FLBoolean))
	}

	def typeBlock(block: Ast.Block, symT: SymbolTable) : Either[String, TypeAst.TypeBlock] = block match {
		case (valExpr:Ast.Expr.Val)::xs => typeVal(valExpr, symT).flatMap{
			case (tyVal, newSymT) => for { 
				tyBlock <- typeBlock(xs,newSymT)
			} yield (tyVal+:tyBlock)
		}
		case expr::xs => for {
			tyExpr <- typeExpr(expr, symT)
			tyBlock <- typeBlock(xs, symT)
		} yield (tyExpr+:tyBlock)
		case Nil => Right(List())
	}
}