package frontend;

import fastparse._
import fastparse.NoWhitespace._


object Statements {
	def CompilationUnit[_ : P] : P[String] = P ( "hello" ).!

	def parse(txt: String) = {
		val tree = fastparse.parse(txt, CompilationUnit(_)).get
		tree.value
	}
}