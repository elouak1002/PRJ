package frontend.struct;

import frontend.struct.Sym._;
import frontend.ast._;

trait SymbolTable

case class Root(table: Map[String, Symbol]) extends SymbolTable
case class Node(table: Map[String, Symbol], parent: SymbolTable) extends SymbolTable

object SymbolTable {

	def getSymbolOfCat(symName: String, symCat: SymbolCat, symT: SymbolTable) : Either[String, Symbol] = {
		lookup(symName,symT) match {
			case Some(Symbol(name,cat,table)) => if (symName==name && symbolCatStrictEquality(symCat,cat)) Right(Symbol(name,cat,table)) else Left("The value " + symName + " is not defined.")
			case None => {
				val catName: String = if (symCat == ValueSym) "value" else "function"
				Left("The " + catName + " " + symName + " is not defined.")
			}
		}
	}
	
	def putSymbol(symName: String, symCat: SymbolCat, symType: FLType, symT: SymbolTable) : Either[String, SymbolTable] = {
		putSymbol(Symbol(symName, symCat, symType),symT)
	}
	
	def putSymbol(symbol: Symbol, symTable: SymbolTable): Either[String,SymbolTable] = {
		if (alreadyDeclared(symbol,symTable,lookupScope)) {
			Left(errorLogging(symbol))
		} else {
			symTable match {
				case Root(map) => Right(Root(map + (symbol.name -> symbol)))
				case Node(map, parent) => Right(Node(map + (symbol.name -> symbol),parent))
			}
		}
	}
	
	def putMultipleSymbol(symbols: Seq[Symbol], symT: SymbolTable): Either[String,SymbolTable] = symbols match {
		case symbol::xs => for {
			newSymT <- putSymbol(symbol,symT)
			symTab <- putMultipleSymbol(xs,newSymT)
		} yield (symTab)
		case Nil => Right(symT)
	}
	
	def errorLogging(t: Symbol) : String = t match {
		case Symbol(name,ValueSym,_) => "Value " + name + " is already defined."
		case Symbol(name,FunctionSym(_),_) => "Function " + name + " is already defined." 
		case Symbol(name,MainSym,_) => "Function " + name + " is already defined."
	}

	def openScope(symTable: SymbolTable) : SymbolTable = {
		Node(Map(),symTable)
	} 
	
	def alreadyDeclared(sym: Symbol, symTable: SymbolTable, f: (String, SymbolTable) => Option[Symbol]) : Boolean = {
		f(sym.name,symTable).map(symbol => Sym.symbolCatEquality(sym, symbol)).getOrElse(false)
	}
	
	def lookup(symbolName: String, symTable: SymbolTable) : Option[Symbol] = symTable match {
		case Root(table) => getFromMap(symbolName,table)
		case Node(table,parent) => if ( getFromMap(symbolName,table) != None) getFromMap(symbolName,table) else lookup(symbolName,parent)
	}

	def lookupScope(symbolName: String, symTable: SymbolTable) : Option[Symbol] = symTable match {
		case Root(table) => getFromMap(symbolName,table)
		case Node(table,parent) => getFromMap(symbolName,table)
	}
	
	def getFromMap(symbol: String, table: Map[String,Symbol]) : Option[Symbol] = {
		try {
			Some(table(symbol))
		} catch {
			case e:Exception => None
		}
	}
}