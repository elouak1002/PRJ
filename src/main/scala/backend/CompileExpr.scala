package backend;

import ast.TypeAst;
import cats.data.State;

case class Label(number: Int) {
	def next = Label(number+1)
}

object CompileExpr {

	// val nextLabel : State[Label, Int] = State(label => (label.next, label.number))

	// def compileIf(expr: TypeAst.TypeExpr.TyIf, nameMap: NameMapper) : (TypeAst.TypeExpr.TyIf,NameMapper) = expr match {
	// 	case TypeAst.TypeExpr.TyIf(bexp, b1, b2, typ) => {
	// 		val (dBexp,m0) = compileBexp(bexp,nameMap)
	// 		val (dB1,m1) = compileBlock(b1,m0)
	// 		val (dB2,m2) = compileBlock(b2,m1)
	// 		(TypeAst.TypeExpr.TyIf(dBexp, dB1, dB2,typ),m2)
	// 	}
	// }

	// def compileAssign(expr: TypeAst.TypeExpr.TyAssign, nameMap: NameMapper) : (TypeAst.TypeExpr.TyAssign,NameMapper) = expr match {
	// 	case TypeAst.TypeExpr.TyAssign(name, args, typ) => {
	// 		val	(dArgs,m0) = compileExprList(args,nameMap)
	// 		(TypeAst.TypeExpr.TyAssign(name,dArgs,typ),m0)
	// 	}
	// }

	// def compileWrite(expr: TypeAst.TypeExpr.TyWrite, nameMap: NameMapper) : (TypeAst.TypeExpr.TyWrite,NameMapper) = expr match {
	// 	case TypeAst.TypeExpr.TyWrite(e, typ) => {
	// 		val (dExpr,m0) = compileExpr(e,nameMap)
	// 		(TypeAst.TypeExpr.TyWrite(dExpr,typ),m0)
	// 	}
	// }

	// def compileAop(expr: TypeAst.TypeExpr.TyAop, nameMap: NameMapper) : (TypeAst.TypeExpr.TyAop,NameMapper) = expr match {
	// 	case TypeAst.TypeExpr.TyAop(op, aexp1, aexp2, typ) => {
	// 		val (dAexp1,m0) = compileExpr(aexp1,nameMap)
	// 		val (dAexp2,m1) = compileExpr(aexp2,m0)
	// 		(TypeAst.TypeExpr.TyAop(op,dAexp1,dAexp2,typ),m1)
	// 	}
	// }

	// def compileValue(expr: TypeAst.TypeExpr.TyValue, nameMap: NameMapper) : (TypeAst.TypeExpr.TyValue,NameMapper) = expr match {
	// 	case TypeAst.TypeExpr.TyValue(str, typ) => {
	// 		val dStr: Option[String] = nameMap.getName(str)
	// 		(TypeAst.TypeExpr.TyValue(dStr.get,typ),nameMap)
	// 	}	
	// }

	// def compileVal(expr: TypeAst.TypeExpr.TyVal, nameMap: NameMapper) : (TypeAst.TypeExpr.TyVal,NameMapper) = expr match {
	// 	case TypeAst.TypeExpr.TyVal(name, expr, typ) => {
	// 		val (m0,dName) = nameMap.addName(name)
	// 		val (dExpr,m1) = compileExpr(expr,m0)
	// 		(TypeAst.TypeExpr.TyVal(dName,dExpr,typ),m1)
	// 	}
	// }

	// def compileBexp(bexp: TypeAst.TypeBexp, nameMap: NameMapper) : (TypeAst.TypeBexp.TyBop,NameMapper) = bexp match {
	// 	case TypeAst.TypeBexp.TyBop(op, aexp1, aexp2, typ) =>
	// 		val (dAexp1,m0) = compileExpr(aexp1,nameMap)
	// 		val (dAexp2,m1) = compileExpr(aexp2,m0)
	// 		(TypeAst.TypeBexp.TyBop(op, dAexp1, dAexp2,typ),m1)
	// }


	// def compileExpr(expr: TypeAst.TypeExpr, nameMap: NameMapper) : (TypeAst.TypeExpr,NameMapper) = expr match {
	// 	case expr: TypeAst.TypeExpr.TyIf => compileIf(expr,nameMap)
	// 	case expr: TypeAst.TypeExpr.TyAssign => compileAssign(expr,nameMap)
	// 	case expr: TypeAst.TypeExpr.TyWrite => compileWrite(expr,nameMap)
	// 	case expr: TypeAst.TypeExpr.TyAop => compileAop(expr,nameMap)
	// 	case expr: TypeAst.TypeExpr.TyValue => compileValue(expr,nameMap)
	// 	case expr: TypeAst.TypeExpr.TyVal => compileVal(expr,nameMap)
	// 	case _ => (expr,nameMap)
	// }


	// def compileExprList(block: TypeAst.TypeBlock, nameMap: NameMapper) : (TypeAst.TypeBlock,NameMapper) = block match {
	// 	case expr::xs => {
	// 		val (compileedExpr,exprMap) = compileExpr(expr,nameMap)
	// 		val recuCall = compileExprList(xs,exprMap)
	// 		(compileedExpr+:recuCall._1,recuCall._2)
	// 	}
	// 	case Nil => (List(),nameMap)
	// }

	// def compileBlock(block: TypeAst.TypeBlock, nameMap: NameMapper) : (TypeAst.TypeBlock,NameMapper) = {
	// 	val blockMap = nameMap.openScope()
	// 	val (compileedBlock, blockMap2) = compileExprList(block, blockMap)
	// 	(compileedBlock,blockMap2.closeScope().get)
	// }

	// def compileDeclBody(block: TypeAst.TypeBlock, nameMap: NameMapper) : TypeAst.TypeBlock = {
	// 	compileBlock(block,nameMap)._1
	// }

}
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