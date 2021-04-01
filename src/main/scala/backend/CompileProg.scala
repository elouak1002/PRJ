package backend;

import backend.JVMOpcode._;
import ast.TypeAst._;
import ast.TypeAst;

object CompileProg {
	
	def compileProg(prog: TypeAst.TypeProg): JVMProg = {
		val funcJ: JVMProgCore = Seq(printIntMethod,printDoubleMethod) ++: prog.map(decl => CompileDecl.compileDecl(decl))
		val headerJ: JVMProgHeader = JVMOpcode.header("XXXX")
		JVMProg(headerJ, funcJ)
	}
	
}