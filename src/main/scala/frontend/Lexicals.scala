package frontend;

import fastparse._
import fastparse.NoWhitespace._

object Lexicals {

	// def lowercase [_ : P] : P[String] = P( CharIn("a-z") ).!
	// def uppercase[_ : P] : P[String] = P( CharIn("A-Z") ).!
	// def letter[_ : P] : P[String]  = P( lowercase | uppercase ).!
	// def digit [_ : P] : P[String]  = P( CharIn("0-9") ).!

	// def Num[_ : P] : P[Ast.Tok.Number] = P ( digit.rep(1)).!.map(_.toInt).map{ case num => Ast.Tok.Number(num)}
	// def Typ[_ : P] : P[Ast.Tok.Type] =  P( ("Int" | "Double" | "Unit")).!.map{ case (typ) => Ast.Tok.Type(typ) }
	// def Iden[_ : P]: P[Ast.Tok.Identifier] = P( letter ~ (letter | digit | "_").repX).!.map{ case iden => Ast.Tok.Identifier(iden)}
}