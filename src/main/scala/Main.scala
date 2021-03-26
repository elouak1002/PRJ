import ast.Ast;
import ast.TypeAst;

import frontend.parser.ProgParser;
import frontend.typer.ProgTyper;

import cats.effect._;
import fastparse._
import scala.io.Source;

object Main {

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

	def compileProg(prog: String) : Either[String, TypeAst.TypeProg] = for {
		tree <- ProgParser.parseProg(prog)
		typeTree <- ProgTyper.typeProg(tree)
	} yield (typeTree)

	def getProgString(args: Array[String]) : IO[Either[String,String]] = {
		getFileName(args) match {
			case Right(filename) => readFile(filename)
			case Left(error) => IO{Left(error)}
		}
	}

	def main(args: Array[String]) : Unit = {

		val progString: IO[Either[String,TypeAst.TypeProg]] = getProgString(args).map({
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