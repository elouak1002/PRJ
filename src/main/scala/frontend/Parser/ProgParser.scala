package frontend.parser;

import fastparse._;
import fastparse.Parsed;
import frontend.ast.Ast;

object ProgParser {

	 def parseProg(prog: String): Either[String, Ast.Prog] = fastparse.parse(prog, Fula.Prog(_)) match {
			case Parsed.Success(tree,_) => Right(tree)
			case Parsed.Failure(_,_,_) => Left("Syntax error.")
	}
}