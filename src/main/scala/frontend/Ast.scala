package frontend;

object Ast {

	sealed trait Tok 
	object Tok {
		case class Identifier(iden: String) extends Tok
		case class Number(num: Int) extends Tok
		case class Type(typ: String) extends Tok
	}

	sealed trait Stmt 
	object Stmt	{
		case class Def(name: String, args: Seq[(String,String)], typ: String, body: Expr) extends Stmt
		case class If(a: Bexp, e1: Expr, e2: Expr) extends Stmt
		case class Main(e: Expr) extends Stmt
	}

	sealed trait Bexp 
	object Bexp {
		case class Bop(o: String, a1: Expr, a2: Expr) extends Bexp
	}

	sealed trait Expr 
	object Expr {
		case class Call(name: String, args: Seq[Expr]) extends Expr
		case class Write(e: Expr) extends Expr
		case class Str(s: String) extends Expr
		case class Num(i: Int) extends Expr
		case class Aop(o: String, a1: Expr, a2: Expr) extends Expr
		case class Sequence(e1: Expr, e2: Expr) extends Expr
	}

}