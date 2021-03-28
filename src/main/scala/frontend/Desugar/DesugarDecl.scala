package frontend.desugar;

import ast.TypeAst._;
import ast.FLType._;
import ast._;

import NameMapper._;

object DesugarDecl {

	def desugarDeclArgs(args: Seq[(String,String)], nameMap: NameMapper): (List[(String,String)],NameMapper) = args match {
		case arg::xs => {
			val (nameMap2, argName) = nameMap.addName(arg._1)
			val argEntry: (String,String) = (argName,arg._2)
			val recurCall = desugarDeclArgs(xs,nameMap2)
			(argEntry+:recurCall._1,recurCall._2)
		}
		case Nil => (List(), nameMap)
	}

	def desugarDecl(decl: TypeAst.TypeDecl) : TypeAst.TypeDecl = decl match {
		case TypeAst.TypeDecl.TyDef(name, args, body, typed)  => {
			val nameMap = NameMapper(Map(), None, 0)
			val (desugaredArgs, nameMap2) = desugarDeclArgs(args,nameMap)
			val desugaredBody: TypeAst.TypeBlock = DesugarExpr.desugarDeclBody(body, nameMap2)
			TypeAst.TypeDecl.TyDef(name, desugaredArgs, desugaredBody, typed)
		}
		case TypeAst.TypeDecl.TyMain(name, body, typed)  => {
			val nameMap = NameMapper(Map(), None, 0)
			val desugaredBody: TypeAst.TypeBlock = DesugarExpr.desugarDeclBody(body, nameMap)
			TypeAst.TypeDecl.TyMain(name, desugaredBody, typed)
		}
	}
}