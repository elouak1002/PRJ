package backend;

import ast.Ast;

object CompileBexp {

	// // compile boolean expressions
	// def compile_bexp(b: BExp, env : Env, jmp: String) : String = b match {
  	// 	case Bop("==", a1, a2) => 
    // 		compile_exp(a1, env) ++ compile_exp(a2, env) ++ i"if_icmpne $jmp"
  	// 	case Bop("!=", a1, a2) => 
    // 		compile_exp(a1, env) ++ compile_exp(a2, env) ++ i"if_icmpeq $jmp"
	// 	case Bop("<", a1, a2) => 
    // 		compile_exp(a1, env) ++ compile_exp(a2, env) ++ i"if_icmpge $jmp"
  	// 	case Bop("<=", a1, a2) => 
    // 		compile_exp(a1, env) ++ compile_exp(a2, env) ++ i"if_icmpgt $jmp"
	// }

}