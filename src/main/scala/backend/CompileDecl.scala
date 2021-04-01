package backend;

import ast.TypeAst._;
import ast.TypeAst;
import ast._;
import ast.FLType._;

import backend.JVMOpcode._;

object CompileDecl {

  def compileDecl(decl: TypeAst.TypeDecl): JVMDecl = decl match {
    case TypeAst.TypeDecl.TyDef(name,_,body,typed) =>  MethodDecl(name, 200, 200, typed, CompileExpr.compileDeclBody(body):+compileReturn(typed))
    case TypeAst.TypeDecl.TyMain(_,body,_) => MainDecl(CompileExpr.compileDeclBody(body):+RETURN)
  }

  def compileReturn(typ: FLType) : Opcode = typ match {
    case FLInt => IRETURN
    case FLDouble => FRETURN
    case FLUnit => RETURN
    case FLFunc(_,funcReturnTyp) => compileReturn(funcReturnTyp)
  }

}