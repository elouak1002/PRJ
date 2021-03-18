package frontend.semantic;

object ExprAnalyzer {

	def resolveExpr(expr: Ast.Expr, symT: SymbolTable) : Either[String, SymbolTable] = expr match {
		case If(bexp,block1,block2) => for {
			_ <- resolveBexp(bexp, symT)
			_ <- resolveBlock(block1, symT)
			_ <- resolveBlock(block2, symT)
		} yield (symT)
		case Assign(name,args) => for {
			_ <- resolveName()
			_ <- resolveArgCall(args, symT)
		} yield (symT)
		case Write(expr) => resolveExpr(expr, symT)
		case Aop(_,a1,a2) => for {
			_ <- resolveExpr(a1, symT)
			_ <- resolveExpr(a2, symT)
		} yield (symT)
		case Value(str) => 
		case Val(name, typ, expr) => 
		case _ => Right(symT)
	} 
	
	/**
	   * Resolve a block (list of expressions) semantic analysis.
	   * @param block
	   * @param symT
	   * @return either a string error or the sym table associated to the block.
	   */
	def resolveBlock(block: Ast.Block, symT: SymbolTable) : Either[String, SymbolTable] = block match {
		case expr::xs => for {
			exprSymT <- resolveExpr(expr, symT)
			symTab <- resolveBlock(xs, exprSymT)
		} yield (symTab)
		case Nil => Right(symT)
	}

	def resolveBexp()


}