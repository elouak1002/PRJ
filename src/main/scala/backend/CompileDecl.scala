package backend;

import ast.Ast;

object CompileDecl {

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