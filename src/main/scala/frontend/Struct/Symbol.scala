package frontend.struct;
import frontend.ast.Ast._;
import frontend.ast._;

trait SymbolCat
case object ValueSym extends SymbolCat
case object MainSym extends SymbolCat
case class FunctionSym(argsNum: Int) extends SymbolCat

trait SymbolType
case object IntSym extends SymbolType
case object DoubleSym extends SymbolType
case object BooleanSym extends SymbolType
case object UnitSym extends SymbolType
case class FuncSym(args: Seq[SymbolType], typ: SymbolType) extends SymbolType

case class Symbol(name: String, symCat: SymbolCat, symTyp: SymbolType)

object Sym {
	
	def createFuncSymType(argsTyp: Seq[String], typ: String) : SymbolType = FuncSym(argsTyp.map(createSymbolType(_)), createSymbolType(typ))

	def createSymbolType(typ: String): SymbolType = typ match {
		case "Int" => IntSym
		case "Double" => DoubleSym
		case "Boolean" => BooleanSym
		case _ => UnitSym
	}

	def createSymbolNode(node: Ast.AstNode) : Symbol = node match {
		case Decl.Def(name,args,typ,_) => Symbol(name,FunctionSym(args.length),createFuncSymType(args.map(_._2),typ))
		case Decl.Main(name,typ,_) => Symbol(name,MainSym,createFuncSymType(Seq(),typ))
		case Expr.Val(name,typ,_) => Symbol(name,ValueSym,createSymbolType(typ))
	}

	def createSymbolArg(node: (String,String)) : Symbol = {
		Symbol(node._1,ValueSym,createSymbolType(node._2))
	}

	val nodesToSymbols = (nodes: Seq[Ast.AstNode]) => nodes.map(node => createSymbolNode(node))
	val argsToSymbols = (nodes: Seq[(String,String)]) => nodes.map(arg => createSymbolArg(arg))

}