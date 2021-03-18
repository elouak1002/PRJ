import frontend.parser.Fula;
import frontend.ast.Ast;

import cats.effect._;
import cats.data.EitherT;
import fastparse._
import scala.io.Source;

object Main {

	def readFile(filename: String) : IO[Either[Unit,String]] = IO {
		try {
		Right(scala.io.Source.fromFile(filename).mkString)
		} 
		catch {case e: Exception => Left(println("No file named " + filename + "."))}
	}
	
	def get_tree(prog: String): IO[Either[Unit,Ast.Prog]] = IO {
		try {
			val tree = fastparse.parse(prog, Fula.Prog(_)).get 
			Right(tree.value) 
		} 
		catch { case e: Exception => Left(println("Syntax error.")) }
	}

	def get_ast(filename: String) : EitherT[IO,Unit,Ast.Prog] = for {
		prog <- EitherT(readFile(filename))
		tree <- EitherT(get_tree(prog))
	} yield tree

	def main(args: Array[String]): Unit = {
		val prog: IO[Unit] = for {
		_ <- get_ast(args(0)).value.flatMap({
						case Right(tree) => IO(println(tree))
						case Left(error) => IO(error)
				})
		} yield ()

		prog.unsafeRunSync()
	}
}