package frontend.parser;

import frontend.ast.Ast;
import fastparse._
import fastparse.MultiLineWhitespace._

/**
  * 
  */
object Fula {

  def FuncDef[_ : P]: P[Ast.Prog] = Declaration.Func.rep(1,";")

	def Prog[_ : P]: P[Ast.Prog] = P (  Declaration.Main.map{ case main => List(main)} ~ ";" | 
                                      (FuncDef ~ ";" ~ Declaration.Main ~ ";").map{ case(funcs,main) => funcs :+ main}
                                    )
}