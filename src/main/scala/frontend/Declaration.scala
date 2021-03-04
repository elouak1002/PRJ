package frontend;

import fastparse._
import fastparse.NoWhitespace._

/**
  * 
  */
object Declaration {

	def Args[_ : P]: P[Seq[(String,String)]] = P ((Lexicals.Identifier.map{ case(Ast.Tok.Identifier(id)) => id } ~ ":" ~ Lexicals.Type.map{ 
		case(Ast.Tok.Type(typ)) => typ }).rep(0,","))

	def Func[_: P]: P[Ast.Decl] = P( "def" ~ Lexicals.Identifier ~ "(" ~ Args ~ ")" ~ ":" ~ Lexicals.Type ~ "=" ~ "{" ~ Expressions.block ~ "}" ).map{
		case(Ast.Tok.Identifier(id),args,Ast.Tok.Type(typ),block) => Ast.Decl.Def(id,args,typ,block)
	}
	
	def Main[_: P]: P[Ast.Decl] = P( "def" ~ "main" ~ "(" ~ ")" ~ ":" ~ "Unit" ~ "=" ~ "{" ~ Expressions.block ~ "}" ).map{
		case(block) => Ast.Decl.Main(body=block)
	}
}