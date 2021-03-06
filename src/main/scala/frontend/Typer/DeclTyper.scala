package frontend.typer;

import frontend.struct._;
import frontend.struct.SymbolTable._;
import frontend.struct.Sym._;

import frontend.typer.TypeChecker._;

import ast.TypeAst._;
import ast.Ast._;
import ast.FLType._;
import ast._;

object DeclTyper {

	/**
	  * Resolve the argument list names of a function.
	  * Arguments are treated as declaration of values in the symbol table. (val arg: argType = ...)
	  */
	 val addArgDecls = (args: Seq[(String, String)], symT: SymbolTable) => SymbolTable.putMultipleSymbol(Sym.argsToSymbols(args),symT)

	 /**
	   * Resolve a declaration.
	   * @param prog
	   * @param symT
	   * @return either a string error or the root symbol table.
	   */
	   def typeFunctionBody(decl: Ast.Decl, symT: SymbolTable) : Either[String, TypeAst.TypeDecl] = decl match {
		case Ast.Decl.Def(name, args, typ, body) => for {
			// Don't need to check the type of the args (as symbol entry has been created using it),
			// only resolve the name.
			argSymT <- addArgDecls(args,symT)
			tyBlock <- ExprTyper.typeBlock(body,argSymT)
			typeOfBlock = getBlockType(tyBlock) // type of a block is the type of the last expression of the block.
			typeOfDef = createSingleType(typ)
			declType <- typeEqual(typeOfBlock,typeOfDef)
		} yield (TypeAst.TypeDecl.TyDef(name,args,tyBlock,createFuncType(args.map(_._2),typ)))
		
		case Ast.Decl.Main(_,_,body) => for {
			tyBlock <- ExprTyper.typeBlock(body,symT)
			typeOfBlock = getBlockType(tyBlock) // type of a block is the type of the last expression of the block.
			mainType <- typeEqual(typeOfBlock,FLUnit)
		} yield (TypeAst.TypeDecl.TyMain(body=tyBlock))
	 }

	 /**
	   * Resolve the list of functions (declarations Def and Main) recursiveley.
	   * @param prog
	   * @param symT
	   * @return
	   */
	 def typeFunctions(prog: Ast.Prog, symT: SymbolTable) : Either[String, TypeAst.TypeProg] = prog match {
		case decl::xs => for {
			func <- typeFunctionBody(decl,openScope(symT))
			funcs <- typeFunctions(xs,symT)
		} yield (func+:funcs)
		case Nil => Right(List())
	}
}