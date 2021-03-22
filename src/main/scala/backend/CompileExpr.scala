package backend;

import ast.Ast;

object CompileExpr {

	// def compile_exp(a: Expr, env: Env) : String = a match {
		
	// 	case Num(i) => i"ldc $i"
		
	// 	case Var(s) => i"iload ${env(s)}"
		
	// 	case Aop(op, a1, a2) => compile_exp(a1, env) ++ compile_exp(a2, env) ++ compile_op(op)
		
	// 	case If(b, a1, a2) => {
	// 		val if_else = NewLabel("If_else")
	// 		val if_end = NewLabel("If_end")
	// 		compile_bexp(b, env, if_else) ++
	// 		compile_exp(a1, env) ++
	// 		i"goto $if_end" ++
	// 		l"$if_else" ++
	// 		compile_exp(a2, env) ++
	// 		l"$if_end"
	// 	}
		
	// 	case Call(name, args) => {
	// 		val is = "I" * args.length
	// 		args.map(a => compile_exp(a, env)).mkString ++
	// 		i"invokestatic XXX/XXX/$name($is)I"
	// 	}
		
	// 	case Sequence(a1, a2) => {
	// 		compile_exp(a1, env) ++ i"pop" ++ compile_exp(a2, env)
	// 	}
	
	// 	case Write(a1) => {
	// 		compile_exp(a1, env) ++
	// 		i"dup" ++
	// 		i"invokestatic XXX/XXX/write(I)V"
	// 	}
	// }

}