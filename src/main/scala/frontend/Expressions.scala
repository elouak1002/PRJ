package frontend;

import fastparse._
import fastparse.MultiLineWhitespace._

object Expressions {

	// def Exp[_ : P]: P[Expr] = 
	// 	P ( P("if" ~ BExp ~ "{" ~ Exp ~ "}" ~ "else" ~ "{" ~ Exp ~ "}").map{ If.tupled } 
	// 	| P(M ~ ";" ~ Exp).map{Sequence.tupled}
	// 	| M )

	// def M[_ : P]: P[Expr] =
	// 	P( P("println" ~ "(" ~ L ~ ")").map(Write) | L )

	// def L[_ : P]: P[Expr] =
	// 	P( P(T ~ "+" ~ Exp).map{ case(x,y) => Aop("+", x, y)} | P(T ~ "-" ~ Exp).map{ case(x,y) => Aop("-", x, y) } | T )

	// def T[_ : P]: P[Expr] =
	// 	P( P(F ~ "*" ~ Exp).map{ case(x,y) => Aop("*", x, y)} | P(F ~ "/" ~ Exp).map{ case(x,y) => Aop("/", x, y) } | P(F ~ "%" ~ Exp).map{ case(x,y) => Aop("%", x, y) } | F )

	// def F[_: P]: P[Expr] = 
	// 	P (	P(Iden ~ "(" ~ Exp.rep(0, ",") ~ ")" ).map{ Call.tupled } | P ("(" ~ Exp ~ ")")  | Iden.map{Str} | Number.map{Num})

	// def BExp[_ : P]: P[BExp] = 
	// P(  P(Exp ~ "==" ~ Exp).map{ case (x, z) => Bop("==", x, z)} 
	// 	| P(Exp ~ "!=" ~ Exp).map{ case (x, z) => Bop("!=", x, z)}  
	// 	| P(Exp ~ "<" ~ Exp).map{ case (x, z) => Bop("<", x, z)}  
	// 	| P(Exp ~ ">" ~ Exp).map{ case (x, z) => Bop("<", z, x)}  
	// 	| P(Exp ~ ">=" ~ Exp).map{ case (x, z) => Bop("<=", z, x)}  
	// 	| P(Exp ~ "<=" ~ Exp).map{ case (x, z) => Bop("<=", x, z)} 
	// 	| "(" ~ BExp ~ ")"  )

	// def Args[_ : P]: P[Seq[(Identifier,Type)]] = P ((Iden ~ ":" ~ Typ).rep(0,","))

	// def Func[_: P]: P[Decl] = P( P( "def" ~/ Iden ~ "(" ~ Args ~ ")" ~ ":" ~ Typ ~ "=" ~ "{" ~ Exp ~ "}" ).map(Def.tupled) )
		
	// def Prog[_: P]: P[List[Decl]] =
	// 	P( P(Func ~ ";" ~ Prog).map{ case (x,y) => x :: y } 
	// 	| P(Exp).map{ case x => List(Main(x)) } )
		
}