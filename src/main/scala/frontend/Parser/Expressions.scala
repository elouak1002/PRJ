package frontend.parser;

import ast.Ast;
import fastparse._
import fastparse.MultiLineWhitespace._

/**
  * 
  */
object Expressions {
	
	// helpers for bexp
	def eq[_ : P] : P[Ast.Bexp] =  P(aexp ~ "==" ~ aexp).map{ case (x, z) => Ast.Bexp.Bop("==", x, z)} 
	def diff[_ : P] : P[Ast.Bexp] =  P(aexp ~ "!=" ~ aexp).map{ case (x, z) => Ast.Bexp.Bop("!=", x, z)} 
	def lt[_ : P] : P[Ast.Bexp] =  P(aexp ~ "<" ~ aexp).map{ case (x, z) => Ast.Bexp.Bop("<", x, z)} 
	def lte[_ : P] : P[Ast.Bexp] =  P(aexp ~ "<=" ~ aexp).map{ case (x, z) => Ast.Bexp.Bop("<=", x, z)} 
	def gt[_ : P] : P[Ast.Bexp] =  P(aexp ~ ">" ~ aexp).map{ case (x, z) => Ast.Bexp.Bop("<", z, x)} 
	def gte[_ : P] : P[Ast.Bexp] =  P(aexp ~ ">=" ~ aexp).map{ case (x, z) => Ast.Bexp.Bop("<=", z, x)} 
	def bexp_paren[_ : P] : P[Ast.Bexp] = P( "(" ~ bexp ~ ")" )

	// bexp
	def bexp[_ : P]: P[Ast.Bexp] = 	P( eq | diff | lt | lte | gt | gte | bexp_paren )

	// parsing of integers, double, boolean and values
	def int[_: P] : P[Ast.Expr.IntExpr] = P (Lexicals.Integer).map{ case( Ast.Tok.IntegerTok(num)) => Ast.Expr.IntExpr(num)}
	def double[_: P] : P[Ast.Expr.DoubleExpr] = P (Lexicals.Double).map{ case( Ast.Tok.DoubleTok(num)) => Ast.Expr.DoubleExpr(num)}
	def boolean[_ : P] : P[Ast.Expr.BooleanExpr] = P(Lexicals.Boolean).map{ case( Ast.Tok.BooleanTok(bool)) => Ast.Expr.BooleanExpr(bool)}
	def value[_ : P] : P[Ast.Expr.Value] = P (Lexicals.Identifier).map{ case(Ast.Tok.Identifier(id)) => Ast.Expr.Value(id) }
	
	//helpers for aexp
	def chainA[_: P](p: => P[Ast.Expr], op: => P[String]) = P( p ~ (op ~ p).rep ).map{case (lhs, rhs) =>rhs.foldLeft(lhs){case (lhs, (op, rhs)) =>Ast.Expr.Aop(op, lhs, rhs)}}
	def aexp_paren[_ : P] : P[Ast.Expr] = P (  "(" ~ aexp ~ ")" )
	def op[_ : P](op: String) = P (op).!

	// aexp 
	def aexp[_ : P] : P[Ast.Expr] = P (chainA(factor,op("+")|op("-")))
	def factor[_ : P] : P[Ast.Expr] = P (chainA(atom,op("*")|op("%")|op("/")))
	def atom[_ : P] : P[Ast.Expr] = P ( double | value | int | aexp_paren )

	// Helper for chaining expr into a seq of expr
	def semi_chain[_ : P](p: => P[Ast.Expr]): P[Seq[Ast.Expr]] = P( p.rep(1,";"))

	// Helper for chaining argument of assign
	def comma_chain[_ : P](p: => P[Ast.Expr]): P[Seq[Ast.Expr]] = P( p.rep(0,",") )
	
	// if expression
	def if_expr[_ : P] : P[Ast.Expr.If] = P ( "if" ~ "(" ~ bexp ~ ")" ~ "{" ~ block ~ "}" ~ "else" ~ "{" ~  block ~ "}").map{
		case (bexp,if_seq,else_seq) => Ast.Expr.If(bexp,if_seq,else_seq)
	}

	// assign expression (calling a function)
	def assign_expr[_ : P]: P[Ast.Expr.Assign] = P ( Lexicals.Identifier ~ "(" ~ comma_chain(expr) ~ ")" ).map{
		case (Ast.Tok.Identifier(id),exprs) => Ast.Expr.Assign(id, exprs)
	}

	// println expression -> by default in the language.
	def write_expr[_ : P]: P[Ast.Expr.Write] = P ("println" ~ "(" ~ expr ~ ")").map{ case (expr) => Ast.Expr.Write(expr) }

	// assigning an expression to a value
	def val_expr[_ : P]: P[Ast.Expr.Val] = P ( "val" ~ Lexicals.Identifier ~ ":" ~ Lexicals.Type ~ "=" ~ expr ).map{ case(Ast.Tok.Identifier(id),Ast.Tok.Type(typ),expr) => Ast.Expr.Val(id,typ,expr) }

	// an expression
	def expr[_ : P]: P[Ast.Expr] = P ( if_expr | write_expr | assign_expr | val_expr | aexp | double | boolean | value | int )

	// an expression block
	def block[_ : P]: P[Ast.Block] = P ( Expressions.semi_chain(Expressions.expr) ~ ";" | Expressions.expr.map{ case expr => List(expr) } ~ ";")

}
