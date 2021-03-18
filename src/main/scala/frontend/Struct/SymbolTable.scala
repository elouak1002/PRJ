package frontend.struct;

import frontend.struct.Sym._;

trait SymbolTable

case class Root(table: Map[String, Symbol]) extends SymbolTable
case class Node(table: Map[String, Symbol], parent: SymbolTable) extends SymbolTable

object SymbolTable {
	
	def putSymbol(symbol: Symbol, symTable: SymbolTable): SymbolTable = symTable match {
		case Root(map) => Root(map + (symbol.name -> symbol))
		case Node(map, parent) => Node(map + (symbol.name -> symbol),parent)
	}
	
	def putMultipleSymbol(symbols: Seq[Symbol], symTable: SymbolTable) : SymbolTable = {
		val symT: SymbolTable = symbols.foldLeft(symTable)((acc,symbol) => putSymbol(symbol,acc))
		symT
	}
	
	def openScope(symTable: SymbolTable) : SymbolTable = {
		Node(Map(),symTable)
	} 
	
	def endScope(symTable: SymbolTable) : Option[SymbolTable] = symTable match {
		case Root(_) => None
		case Node(_,parent) => Some(parent)
	}

	def lookupSymbol(symTable: SymbolTable, symIn: Symbol, f: (SymbolTable, String) => Option[Symbol]) : Option[Symbol] = {
		f(symTable,symIn.name).flatMap(sym => if (symIn == sym) Some(sym) else None)
	}
	
	def lookup(symTable: SymbolTable, symbol: String) : Option[Symbol] = symTable match {
		case Root(table) => getFromMap(table,symbol)
		case Node(table,parent) => if ( getFromMap(table,symbol) != None) getFromMap(table,symbol) else lookup(parent,symbol)
	}

	def lookupScope(symTable: SymbolTable, symbol: String) : Option[Symbol] = symTable match {
		case Root(table) => getFromMap(table,symbol)
		case Node(table,parent) => getFromMap(table,symbol)
	}
	
	def getFromMap(table: Map[String,Symbol], symbol: String) : Option[Symbol] = {
		try {
			Some(table(symbol))
		} catch {
			case e:Exception => None
		}
	}
}