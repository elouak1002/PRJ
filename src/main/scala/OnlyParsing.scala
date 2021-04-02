import ast.Ast;
import ast.TypeAst;

import frontend.parser.ProgParser;
import frontend.typer.ProgTyper;
import frontend.desugar.DesugarProg;

import cats.effect._;
import fastparse._
import scala.io.Source;

import jasmin.{Main => Jasmin};

object OnlyParsing {

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

	def getTree(prog: String) : Either[String, Ast.Prog] = for {
		tree <- ProgParser.parseProg(prog)
		// typeTree <- ProgTyper.typeProg(tree)
		// desugaredTree = DesugarProg.desugarProg(typeTree)
	} yield (tree)

	def getProgString(args: Array[String]) : IO[Either[String,String]] = {
		getFileName(args) match {
			case Right(filename) => readFile(filename)
			case Left(error) => IO{Left(error)}
		}
	}

	def main(args: Array[String]) : Unit = {

		val progString: IO[Either[String,Ast.Prog]] = getProgString(args).map({
			case Right(progStr) => getTree(progStr)
			case Left(error) => Left(error)
		})

		val compile: IO[Unit] = progString.flatMap({
			case Right(tree) => IO{println(tree)}
			case Left(error) => IO{println(error)}
		})

		compile.unsafeRunSync()
	}
}