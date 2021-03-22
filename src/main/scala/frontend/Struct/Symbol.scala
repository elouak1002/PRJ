package frontend.struct;
import frontend.ast.Ast._;
import frontend.ast.FLType._;
import frontend.ast._;

trait SymbolCat
case object ValueSym extends SymbolCat
case object MainSym extends SymbolCat
case class FunctionSym(argsNum: Int) extends SymbolCat

case class Symbol(name: String, symCat: SymbolCat, symTyp: FLType)

object Sym {

	def createSymbolNode(node: Ast.AstNode) : Symbol = node match {
		case Decl.Def(name,args,typ,_) => Symbol(name,FunctionSym(args.length),createFuncType(args.map(_._2),typ))
		case Decl.Main(name,typ,_) => Symbol(name,MainSym,createFuncType(Seq(),typ))
		case Expr.Val(name,typ,_) => Symbol(name,ValueSym,createSingleType(typ))
	}

	def createSymbolArg(node: (String,String)) : Symbol = {
		Symbol(node._1,ValueSym,createSingleType(node._2))
	}

	def symbolCatEquality(sym1: Symbol,sym2: Symbol): Boolean = symbolCatEquality(sym1.symCat,sym2.symCat)

	def symbolCatEquality(symCat1: SymbolCat, symCat2: SymbolCat) : Boolean = (symCat1,symCat2) match {
		case (ValueSym,ValueSym) => true
		case (MainSym,MainSym) => true
		case (cat1:FunctionSym,cat2:FunctionSym) => true
		case (cat1:FunctionSym,MainSym) => true
		case (MainSym,cat2:FunctionSym) => true
		case _ => false
	}

	def symbolCatStrictEquality(symCat1: SymbolCat, symCat2: SymbolCat) : Boolean = (symCat1,symCat2) match {
		case (ValueSym,ValueSym) => true
		case (MainSym,MainSym) => true
		case (FunctionSym(n1),FunctionSym(n2)) => if (n1==n2) true else false
		case _ => false
	}

	val nodesToSymbols = (nodes: Seq[Ast.AstNode]) => nodes.map(node => createSymbolNode(node))
	val argsToSymbols = (nodes: Seq[(String,String)]) => nodes.map(arg => createSymbolArg(arg))

}