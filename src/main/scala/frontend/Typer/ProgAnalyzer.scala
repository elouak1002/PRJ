package frontend.semantic;

import frontend.ast.Ast._;
import frontedn.semantic.SemanticChecker._;

object ProgTyper {

	/**
	  * Resolve only the declaration of the functions, not the body.
	  */
	 val addFuncDecls = (nodes: Seq[Ast.Decl], symT: SymbolTable) => resolveDeclList(nodesToSymbols(nodes),symT)

	 /**
	   * Resolve an entire program semantic analysis.
	   * @param prog
	   * @return either a string error if the table can't pass the semantic analysis,
	   * or the sym table associated to the program.
	   */
	 def resolveProg(prog: Ast.Prog): Either[String, SymbolTable] = {
		val root: SymbolTable = Root(Map())
		for {
			rootSym <- addFuncDecls(prog, symT)
			symT <- typeFunction(prog, rootSym)
		} yield (symT)
	}

}

// val test: List[(String,String)] = List(("name1","Int"),("name2","Double"),("name3","Boolean"),("name3","Unit"))
// resolveArgList(test,Root(Map()))

// val prog: Ast.Prog = List(Def("fiba",List(),"Int",List(Val("x","Int",IntExpr(2)), If(Bop("==",Value("x"),IntExpr(2)),List(Val("y","Int",IntExpr(20)), Value("y")),List(Val("y","Int",IntExpr(30)), Value("y"))))),Def("fib",List(),"Int",List(Val("x","Int",IntExpr(2)), If(Bop("==",Value("x"),IntExpr(2)),List(Val("y","Int",IntExpr(20)), Value("y")),List(Val("y","Int",IntExpr(30)), Value("y"))))),Main("main","Unit",List(Write(Assign("fib",List())))))
// resolveFuncList(prog,Root(Map()))

// for {
// 	_ <- test1(1)
// 	_ <- test2(1)
// 	symT <- test2(2)
// } yield(symT)

// def test2(x: Double) : Either[String,Double] = x match {
// 	case 1.0 => Right(1.0)
// 	case _ => Left("Error")
// }

// val test = List(("val1","typ1"),("val2","typ2"),("val3","typ3"))
// test.map(_._1)
