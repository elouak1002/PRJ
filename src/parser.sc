import fastparse._
import MultiLineWhitespace._

{
abstract class Expr
abstract class BExp 
abstract class Decl 

case class Def(name: String, args: Seq[(String,String)], typ: String, body: Expr) extends Decl
case class Main(e: Expr) extends Decl

case class Call(name: String, args: Seq[Expr]) extends Expr
case class If(a: BExp, e1: Expr, e2: Expr) extends Expr
case class Write(e: Expr) extends Expr
case class Var(s: String) extends Expr
case class Num(i: Int) extends Expr
case class Aop(o: String, a1: Expr, a2: Expr) extends Expr
case class Sequence(e1: Expr, e2: Expr) extends Expr
case class Bop(o: String, a1: Expr, a2: Expr) extends BExp


def lowercase [_ : P] = P( CharIn("a-z") )
def uppercase[_ : P]  = P( CharIn("A-Z") )
def letter[_ : P]     = P( lowercase | uppercase )
def digit [_ : P]     = P( CharIn("0-9") )

def Typ[_ : P]: P[String] =  P( ("Int" | "Double" | "Unit") ~ End ).!

def Number[_ : P]: P[Int] =  P( digit.rep(1) ).!.map(_.toInt)
def Iden[_ : P]: P[String] = P( letter ~ (letter | digit | "_").rep ).!

def Exp[_ : P]: P[Expr] = 
	P ( P("if" ~ BExp ~ "{" ~ Exp ~ "}" ~ "else" ~ "{" ~ Exp ~ "}").map{ If.tupled } 
	  | P(M ~ ";" ~ Exp).map{Sequence.tupled}
	  | M )


def M[_ : P]: P[Expr] =
	P( P("println" ~ "(" ~ L ~ ")").map(Write) | L )

def L[_ : P]: P[Expr] =
	P( P(T ~ "+" ~ Exp).map{ case(x,y) => Aop("+", x, y)} | P(T ~ "-" ~ Exp).map{ case(x,y) => Aop("-", x, y) } | T )

def T[_ : P]: P[Expr] =
	P( P(F ~ "*" ~ Exp).map{ case(x,y) => Aop("*", x, y)} | P(F ~ "/" ~ Exp).map{ case(x,y) => Aop("/", x, y) } | P(F ~ "%" ~ Exp).map{ case(x,y) => Aop("%", x, y) } | F )

def F[_: P]: P[Expr] = 
	P (	P(Iden ~ "(" ~ Exp.rep(0, ",") ~ ")" ).map{ Call.tupled } | P ("(" ~ Exp ~ ")")  | Iden.map{Var} | Number.map{Num})

def BExp[_ : P]: P[BExp] = 
  P(  P(Exp ~ "==" ~ Exp).map{ case (x, z) => Bop("==", x, z)} 
    | P(Exp ~ "!=" ~ Exp).map{ case (x, z) => Bop("!=", x, z)}  
    | P(Exp ~ "<" ~ Exp).map{ case (x, z) => Bop("<", x, z)}  
    | P(Exp ~ ">" ~ Exp).map{ case (x, z) => Bop("<", z, x)}  
    | P(Exp ~ ">=" ~ Exp).map{ case (x, z) => Bop("<=", z, x)}  
	| P(Exp ~ "<=" ~ Exp).map{ case (x, z) => Bop("<=", x, z)} 
	| "(" ~ BExp ~ ")"  )

def Func[_: P]: P[Decl] = P( P( "def" ~/ Iden ~ "(" ~ Args.rep(0,",") ~ ")" ~ ":" ~ Typ ~ "=" ~ "{" ~ Exp ~ "}" ).map(Def.tupled) )
}

def Prog[_: P]: P[List[Decl]] =
	P( P(Func ~ ";" ~ Prog).map{ case (x,y) => x :: y } 
	| P(Exp).map{ case x => List(Main(x)) } )

import ammonite.ops._

def get_tree(fname: String): List[Decl] = {
	val prog = os.read(os.pwd / os.RelPath(fname) )
	val tree = fastparse.parse(prog, Prog(_)).get
	tree.value
}


@main
def main(fname: String): Unit = {
	println(get_tree(fname))
}


val prog = """def fib(index: Int) : Int = {
  if (index <= 0) {
    current
  } else {
    fib(index - 1, prev + current, prev)
  }
}
"""

val test = "index : Int"

def Args[_ : P]: P[(String,String)] = P (Iden ~ ":" ~ Typ).map{
	case (id,typ) => (id,typ)
}

def Test2[_ : P] = P (Args ~ "," ~ Args )

fastparse.parse(test, Args(_))