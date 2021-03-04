package frontend;

import fastparse._
import fastparse.NoWhitespace._

/**
  * 
  */
object Fula {

	def Prog[_ : P]: P[Ast.Prog] = P (  Declaration.Func.rep(0,";") ~ Declaration.Main ~ ";"  ).map{
    case(funcs,main) => funcs :+ main
  }

}