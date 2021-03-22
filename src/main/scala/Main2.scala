import frontend.ast.Ast;
import frontend.ast.TypeAst;

import frontend.parser.ProgParser;
import frontend.typer.ProgTyper;

import cats.effect._;
import cats.data.EitherT;
import fastparse._
import scala.io.Source;

object Main2 {

	def readFile(filename: String) : IO[Either[String,String]] = IO {
		try {
		Right(scala.io.Source.fromFile(filename).mkString)
		} 
		catch {case e: Exception => Left("No file named " + filename + ".")}
	}

	def getFileName(args: Array[String]) : Either[String, String] = {
		try { Right(args(0)) }
		catch {case e: Exception => Left("You must enter a filename.")}
	}

	def compileProg(prog: String) : Either[String, Ast.Prog] = for {
		tree <- ProgParser.parseProg(prog)
		// typeTree <- ProgTyper.typeProg(tree)
	} yield (tree)

	def getProgString(args: Array[String]) : IO[Either[String,String]] = {
		getFileName(args) match {
			case Right(filename) => readFile(filename)
			case Left(error) => IO{Left(error)}
		}
	}

	def main(args: Array[String]) : Unit = {

		val progString: IO[Either[String,Ast.Prog]] = getProgString(args).map({
			case Right(progStr) => compileProg(progStr)
			case Left(error) => Left(error)
		})

		val compile: IO[Unit] = progString.flatMap({
			case Right(tree) => IO{println(tree)}
			case Left(error) => IO{println(error)}
		})

		compile.unsafeRunSync()
	}

}