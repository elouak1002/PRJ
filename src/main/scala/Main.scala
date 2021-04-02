import ast.Ast;
import ast.TypeAst;
import backend.JVMOpcode;
import backend.JVMOpcode._;

import frontend.parser.ProgParser;
import frontend.typer.ProgTyper;
import frontend.mangler.MangleProg;
import backend.CompileProg;
import backend.CompileJasmin;

import cats.effect._;
import cats.data._;
import scala.io.Source;

import jasmin.{Main => Jasmin};

object Main {

	def readFile(filename: String) : IO[Either[String,String]] = IO {
		try {
		Right(scala.io.Source.fromFile(filename).mkString)
		} 
		catch {case e: Exception => Left("No file named " + filename + ".")}
	}

	def getFileName(args: Array[String]) : Either[String, String] = {
		try { 
			val filename = args(0)
			if (filename.endsWith(".fula")) Right(filename) else Left("Error: Must be fula file.")
		}
		catch {case e: Exception => Left("You must enter a filename.")}
	}

	def getTree(prog: String) : Either[String, TypeAst.TypeProg] = for {
		tree <- ProgParser.parseProg(prog)
		typeTree <- ProgTyper.typeProg(tree)
		mangledTree = MangleProg.mangleProg(typeTree)
	} yield (mangledTree)

	def compileTree(tree: TypeAst.TypeProg, filename: String): String = {
		val className = filename.replaceAll(".+/","").stripSuffix(".fula")
		val jvmProg = CompileProg.compileProg(tree).toString().replace("XXXX",className)
		jvmProg
	}

	def main(args: Array[String]) : Unit = {

		val filename : Either[String,String] = getFileName(args)

		val progStr : IO[Either[String,String]] = filename match {
			case Right(file) => readFile(file)
			case Left(error) => IO{Left(error)}
		}

		val progCompiled: IO[Either[String,String]] = progStr.map(x => for {
			prog <- x
			tree <- getTree(prog)
			name <- filename
			jvmProg = compileTree(tree,name)
		} yield (jvmProg))


		val compileByteCode: EitherT[IO,String,Unit] = for {
			jasminProg <- EitherT(progCompiled)
			filename <- EitherT(CompileJasmin.createTemporaryJasminFile(jasminProg))
			_ <- EitherT(CompileJasmin.writeByteCode(filename))
		} yield ()

		compileByteCode.value.flatMap({
			case Right(io) => IO(io)
			case Left(error) => IO(println(error))
		}).unsafeRunSync
	}
}