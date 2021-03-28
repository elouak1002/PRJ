package frontend.desugar;

import ast.TypeAst._;
import ast.FLType._;
import ast._;

object DesugarProg {

	def desugarProg(prog: TypeAst.TypeProg) : TypeAst.TypeProg = prog.map(decl => DesugarDecl.desugarDecl(decl))

}