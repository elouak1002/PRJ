package frontend.ast;

/**
  * 
  */
object Ast {

	// Tokens used for preparsing (form of lexer)
	sealed trait Tok 
	object Tok {
		case class Identifier(iden: String) extends Tok
		case class IntegerTok(num: Int) extends Tok
		case class DoubleTok(num: Double) extends Tok
		case class BooleanTok(num: Boolean) extends Tok
		case class Type(typ: String) extends Tok
	}

	// A sequence of expression semi-column separated is a block
	type Block = Seq[Expr]
	
	// A boolean expression
	sealed trait Bexp
	object Bexp {
		case class Bop(o: String, a1: Expr, a2: Expr) extends Bexp
	}

	sealed trait Expr 
	object Expr {
		case class If(a: Bexp, e1: Block, e2: Block) extends Expr
		case class Assign(name: String, args: Seq[Expr]) extends Expr
		case class Value(s: String) extends Expr
		case class Write(e: Expr) extends Expr
		case class IntExpr(i: Int) extends Expr
		case class DoubleExpr(d: Double) extends Expr
		case class BooleanExpr(b: Boolean) extends Expr
		case class Aop(o: String, a1: Expr, a2: Expr) extends Expr
		case class Val(name: String, typ: String, e: Expr) extends Expr
	}
	
	sealed trait Decl 
	object Decl	{
		case class Def(name: String, args: Seq[(String,String)], typ: String, body: Block) extends Decl
		case class Main(name: String="main", typ:String="Unit", body: Block) extends Decl
	}
	
	type Prog = Seq[Decl]
}