package frontend.semantic;

object DeclAnalyzer {

	/**
	  * Resolve the argument list names of a function.
	  * Arguments are treated as declaration of values in the symbol table. (val arg: argType = ...)
	  */
	 val resolveArgList = (args: Seq[(String, String)], symT: SymbolTable) => resolveDeclList(argsToSymbols(args),symT)

	 /**
	   * Resolve a declaration.
	   * @param prog
	   * @param symT
	   * @return either a string error or the root symbol table.
	   */s
	 def resolveDeclBody(decl: Ast.Decl, symT: SymbolTable) : Either[String, SymbolTable] = decl match {
		case Def(_, args, _, body) => for {
 			argSymT <- resolveArgList(args,symT)
			_ <- resolveBlock(body,argSymT)
			symTab <- resolveDeclBody(xs,symT)
		} yield (symTab)
		case Main(_,_,block) => for {
			blockSymT <- resolveFuncBlock(body,symT)
		} yield (symT)
	 }

	 /**
	   * Resolve the list of functions (declarations Def and Main) recursiveley.
	   * @param prog
	   * @param symT
	   * @return
	   */
	 def resolveFuncsBody(prog: Ast.Prog, symT: SymbolTable) : Either[String, SymbolTable] = {
		case decl::xs => for {
			eleSymT <- resolveDeclBody(decl,openScope(symT))
			symTab <- resolveFuncsBody(xs,symT)
		} yield (symTab)
		case Nil => Right(symT)
	}

}