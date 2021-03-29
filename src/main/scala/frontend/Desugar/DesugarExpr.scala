package frontend.desugar;

import ast.TypeAst._;
import ast.FLType._;
import ast._;

object DesugarExpr {

	def desugarIf(expr: TypeAst.TypeExpr.TyIf, nameMap: NameMapper) : (TypeAst.TypeExpr.TyIf,NameMapper) = expr match {
		case TypeAst.TypeExpr.TyIf(bexp, b1, b2, typ) => {
			val (dBexp,m0) = desugarBexp(bexp,nameMap)
			val (dB1,m1) = desugarBlock(b1,m0)
			val (dB2,m2) = desugarBlock(b2,m1)
			(TypeAst.TypeExpr.TyIf(dBexp, dB1, dB2,typ),m2)
		}
	}

	def desugarAssign(expr: TypeAst.TypeExpr.TyAssign, nameMap: NameMapper) : (TypeAst.TypeExpr.TyAssign,NameMapper) = expr match {
		case TypeAst.TypeExpr.TyAssign(name, args, typ) => {
			val	(dArgs,m0) = desugarExprList(args,nameMap)
			(TypeAst.TypeExpr.TyAssign(name,dArgs,typ),m0)
		}
	}

	def desugarWrite(expr: TypeAst.TypeExpr.TyWrite, nameMap: NameMapper) : (TypeAst.TypeExpr.TyWrite,NameMapper) = expr match {
		case TypeAst.TypeExpr.TyWrite(e, typ) => {
			val (dExpr,m0) = desugarExpr(e,nameMap)
			(TypeAst.TypeExpr.TyWrite(dExpr,typ),m0)
		}
	}

	def desugarAop(expr: TypeAst.TypeExpr.TyAop, nameMap: NameMapper) : (TypeAst.TypeExpr.TyAop,NameMapper) = expr match {
		case TypeAst.TypeExpr.TyAop(op, aexp1, aexp2, typ) => {
			val (dAexp1,m0) = desugarExpr(aexp1,nameMap)
			val (dAexp2,m1) = desugarExpr(aexp2,m0)
			(TypeAst.TypeExpr.TyAop(op,dAexp1,dAexp2,typ),m1)
		}
	}

	def desugarValue(expr: TypeAst.TypeExpr.TyValue, nameMap: NameMapper) : (TypeAst.TypeExpr.TyValue,NameMapper) = expr match {
		case TypeAst.TypeExpr.TyValue(str, typ) => {
			val dStr: Option[String] = nameMap.getName(str)
			(TypeAst.TypeExpr.TyValue(dStr.get,typ),nameMap)
		}	
	}

	def desugarVal(expr: TypeAst.TypeExpr.TyVal, nameMap: NameMapper) : (TypeAst.TypeExpr.TyVal,NameMapper) = expr match {
		case TypeAst.TypeExpr.TyVal(name, expr, typExpr, typ) => {
			val (m0,dName) = nameMap.addName(name)
			val (dExpr,m1) = desugarExpr(expr,m0)
			(TypeAst.TypeExpr.TyVal(dName, dExpr,typExpr, typ),m1)
		}
	}

	def desugarBexp(bexp: TypeAst.TypeBexp, nameMap: NameMapper) : (TypeAst.TypeBexp.TyBop,NameMapper) = bexp match {
		case TypeAst.TypeBexp.TyBop(op, aexp1, aexp2, typ) =>
			val (dAexp1,m0) = desugarExpr(aexp1,nameMap)
			val (dAexp2,m1) = desugarExpr(aexp2,m0)
			(TypeAst.TypeBexp.TyBop(op, dAexp1, dAexp2,typ),m1)
	}


	def desugarExpr(expr: TypeAst.TypeExpr, nameMap: NameMapper) : (TypeAst.TypeExpr,NameMapper) = expr match {
		case expr: TypeAst.TypeExpr.TyIf => desugarIf(expr,nameMap)
		case expr: TypeAst.TypeExpr.TyAssign => desugarAssign(expr,nameMap)
		case expr: TypeAst.TypeExpr.TyWrite => desugarWrite(expr,nameMap)
		case expr: TypeAst.TypeExpr.TyAop => desugarAop(expr,nameMap)
		case expr: TypeAst.TypeExpr.TyValue => desugarValue(expr,nameMap)
		case expr: TypeAst.TypeExpr.TyVal => desugarVal(expr,nameMap)
		case _ => (expr,nameMap)
	}


	def desugarExprList(block: TypeAst.TypeBlock, nameMap: NameMapper) : (TypeAst.TypeBlock,NameMapper) = block match {
		case expr::xs => {
			val (desugaredExpr,exprMap) = desugarExpr(expr,nameMap)
			val recuCall = desugarExprList(xs,exprMap)
			(desugaredExpr+:recuCall._1,recuCall._2)
		}
		case Nil => (List(),nameMap)
	}

	def desugarBlock(block: TypeAst.TypeBlock, nameMap: NameMapper) : (TypeAst.TypeBlock,NameMapper) = {
		val blockMap = nameMap.openScope()
		val (desugaredBlock, blockMap2) = desugarExprList(block, blockMap)
		(desugaredBlock,blockMap2.closeScope().get)
	}

	def desugarDeclBody(block: TypeAst.TypeBlock, nameMap: NameMapper) : TypeAst.TypeBlock = {
		desugarBlock(block,nameMap)._1
	}
}

