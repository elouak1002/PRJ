package backend;

import jasmin.{Main => Jasmin};
import jas.StackMap;

import cats.data.EitherT;
import cats.effect._;

object CompileJasmin {

	def createTemporaryJasminFile(prog: String) : IO[Either[String,String]] = IO {
		try {
			val file = os.temp(contents=prog, prefix="pre-fula-",suffix=".txt")
			Right(file.toString())
		} catch {case e: Exception => Left("Error: cannot write file.")}
	}

	def writeByteCode(jasminFilePath: String) : IO[Either[String,Unit]] = IO {
		try {
			val jasmin: Jasmin = new Jasmin()
			Right(jasmin.assemble(jasminFilePath))
		} catch {case e: Exception => Left("Error: cannot write file.")}
	}

}