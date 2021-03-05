package frontend.parser;

import frontend.ast.Ast;
import fastparse._
import fastparse.MultiLineWhitespace._

/**
  * 
  */
object Lexicals {

 	val keywordList: Set[String] = Set("if", "else", "def", "main", "val", "println")

	 // Tokens
	def lowercase [_ : P] : P[String] = P( CharIn("a-z") ).!
	def uppercase[_ : P] : P[String] = P( CharIn("A-Z") ).!
	def letter[_ : P] : P[String]  = P( lowercase | uppercase ).!
	def digit [_ : P] : P[String]  = P( CharIn("0-9") ).!
	def unsignedIntegerStr[_ : P] : P[String] = P( digit.rep(1) ).!
	// Signed Integer to String parser
	def IntegerStr[_ : P] : P[String] = P ( "-".?.! ~ unsignedIntegerStr).!
	def BooleanStr[_ : P] : P[String] = P ("True" | "False").!

	// A boolean value
	def Boolean[_ : P] : P[Ast.Tok.BooleanTok] = P(BooleanStr).map(_.toBoolean).map{ case bool => Ast.Tok.BooleanTok(bool) }
	// An integer
	def Integer[_ : P] : P[Ast.Tok.IntegerTok] = P(IntegerStr).map(_.toInt).map{ case num => Ast.Tok.IntegerTok(num)}
	// A double
	def Double[_ : P] : P[Ast.Tok.DoubleTok] = P (  IntegerStr ~ "." ~ unsignedIntegerStr ).!.map(_.toDouble).map{ case num => Ast.Tok.DoubleTok(num)}
	// A type
	def Type[_ : P] : P[Ast.Tok.Type] =  P( ("Int" | "Double" | "Unit")).!.map{ case (typ) => Ast.Tok.Type(typ) }
	// An identifier (name of function, name of value)
	def Identifier[_ : P]: P[Ast.Tok.Identifier] = P( letter ~ (letter | digit | "_").repX).!.filter(!keywordList.contains(_)).map{ case iden => Ast.Tok.Identifier(iden)}
	
}