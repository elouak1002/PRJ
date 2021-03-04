import frontend.Fula;
import frontend.Ast;

import cats.effect.IO;
import fastparse._
import scala.io.Source;

object Main {

	def readFile(filename: String) : IO[String] = IO {
		scala.io.Source.fromFile(filename).mkString
	}
	
	def get_tree(prog: String): Ast.Prog = {
		val tree = fastparse.parse(prog, Fula.Prog(_)).get
		tree.value
	}

	def putTree(tree: Ast.Prog) : IO[Unit] = IO { println(tree) }

	def putStrLn(line: String): IO[Unit] = IO { println(line) }

	def manipulate_ars(args: Array[String]) = args.length 

	def main(args: Array[String]): Unit = {
		val prog = for {
			file <- readFile(args(0))
			tree = get_tree(file)
			_ <- putTree(tree)
		} yield ()

		prog.unsafeRunSync()
	}
}