package frontend.semantic;

import frontend.struct._;
import frontend.struct.SymbolTable._;
import frontend.struct.Symbol._;

object SemanticChecker {

	/**
	  * @param sym
	  * @param symT
	  * @return either a string error if a name is already declared,
	  * or add the symbol table with the added symbol.
	  */
	def resolveDecl(sym: Symbol, symT: SymbolTable) : Either[String, SymbolTable] = {
		if (alreadyDeclared(symT,sym,lookupScope)) {
			Left("Name " + sym.name + " is already declared.")
		} else {
			Right(putSymbol(sym,symT))
		}
	}

	def resolveDeclList(elems: Seq[Symbol], symT: SymbolTable): Either[String,SymbolTable] = elems match {
		case ele::xs => for {
			eleSymT <- resolveDecl(ele,symT)
			symTab <- resolveDeclList(xs,eleSymT)
		} yield (symTab)
		case Nil => Right(symT)
	}



	def resolveNameCat(symName: String, symCat: SymbolCat) : Either[String, SymbolTable] = {
		
	}

	 /**
	   * @param sym
	   * @param symT
	   * @return either a string error if a name is not declared,
	   * or the symbol table if the name is declared.
	   */
	 def resolveName(sym: Symbol, symT: SymbolTable) : Either[String, SymbolTable] = {
		 if (alreadyDeclared(symT,sym,lookup)) {
			Right(symT)
		 } else {
			Left("Name " + sym.name + " is not declared.")
		 }
	 }

	/**
	  * Check if a symbol with same name and category has
	  * already been declared. 
	  * The f function determine the scope search.
	  * @param symTable
	  * @param sym
	  * @param symCat
	  * @param f
	  * @return 
	  */
	def alreadyDeclared(symTable: SymbolTable, sym: Symbol, f: (SymbolTable, String) => Option[Symbol]) : Boolean = {
		f(symTable,sym.name).map(symbol => catEquality(sym, symbol)).getOrElse(false)
	}

	/**
	  * Determine the equality between two symbols in terms
	  * of their categories.
	  * @param sym1
	  * @param sym2
	  * @return
	  */
	def catEquality(sym1: Symbol, sym2: Symbol): Boolean = {
		if  (sym1.symCat == sym2.symCat || 
			(sym1.symCat == FunctionSym && sym2.symCat == MainSym) || 
			(sym1.symCat == MainSym && sym2.symCat == FunctionSym)) true 
		else false
	}




}