package backend;

import ast.TypeAst;

object CompileDecl {

  // def compileDef(func: TypeAst.TypeDecl.TyDef) : String = func match {
  //   case TypeAst.TypeDecl.TyDef(name,args,body,typed) => {
  //     functionHeader(name,typed)
  //     local(args,body)
  //     stackSize(args,body)
  //     compileDeclBody(body)
  //     compileReturn(typed)
  //   }
  // }
    
    
  // def compileMain(main: TypeAst.TypeDecl.TyMain) : String = {
  //   case TypeAst.TypeDecl.TyMain(name,body,typed) => 
  // }

  // def compileDecl(prog: TypeAst.TypeProg): List[String] = prog match {
  //   case (func:TypeAst.TypeDecl.TyDef)::xs => compileDef(func)::compileFunctions(xs)

  //   case (main:TypeAst.TypeDecl.TyMain)::xs => compileMain(main)::compileFunctions(xs)

  //   case Nil => List("")

  // }
  
  // // compile functions and declarations
  // def compile_decl(d: Decl) : String = d match {
  	// 	case Def(name, args, a) => { 
    // 		val env = args.zipWithIndex.toMap
    // 		val is = "I" * args.length
    // 		m".method public static $name($is)I" ++
    // 		m".limit locals ${args.length}" ++
    // 		m".limit stack ${1 + estimate_exp_stack(a)}" ++
    // 		l"${name}_Start" ++   
    // 		compile_exp(a, env) ++
    // 		i"ireturn" ++
    // 		m".end method\n"
    // 	}
  
  	// 	case Main(a) => {
 	//    		m".method public static main([Ljava/lang/String;)V" ++
    // 		m".limit locals 1" ++
    // 		m".limit stack ${1 + estimate_exp_stack(a)}" ++
    // 		compile_exp(a, Map()) ++
    // 		i"return" ++
    // 		m".end method\n"
  	// 	}
	
	// }
}