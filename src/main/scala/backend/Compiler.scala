// def estimate_exp_stack(e: Expr): Int = e match {
//   case Call(_, args) => args.map(estimate_exp_stack).sum
//   case If(a, e1, e2) => 
//     estimate_bexp_stack(a) + (List(estimate_exp_stack(e1), estimate_exp_stack(e2)).max)
//   case Write(e) => estimate_exp_stack(e) + 1
//   case Var(_) => 1
//   case Num(_) => 1
//   case Aop(_, a1, a2) => estimate_exp_stack(a1) + estimate_exp_stack(a2)
//   case Sequence(e1, e2) => List(estimate_exp_stack(e1), estimate_exp_stack(e2)).max
// }

// def estimate_bexp_stack(e: BExp): Int = e match {
//   case Bop(_, a1, a2) => estimate_exp_stack(a1) + estimate_exp_stack(a2)
// }


// val start = """
// .class public XXX.XXX
// .super java/lang/Object

// .method public static write(I)V 
//     .limit locals 1 
//     .limit stack 2 
//     getstatic java/lang/System/out Ljava/io/PrintStream; 
//     iload 0
//     invokevirtual java/io/PrintStream/println(I)V
//     return 
// .end method

// """

// var counter = -1

// def NewLabel(x: String) = {
//   counter += 1
//   x ++ "_" ++ counter.toString()
// }

// import scala.language.implicitConversions
// import scala.language.reflectiveCalls

// implicit def sring_inters(sc: StringContext) = new {
//   def i(args: Any*): String = "   " ++ sc.s(args:_*) ++ "\n"  
//   def l(args: Any*): String = sc.s(args:_*) ++ ":\n"
//   def m(args: Any*): String = sc.s(args:_*) ++ "\n"
// }

// // variable / index environments
// type Env = Map[String, Int]

// def compile_op(op: String) = op match {
//   case "+" => i"iadd"
//   case "-" => i"isub"
//   case "*" => i"imul"
//   case "/" => i"idiv"
//   case "%" => i"irem"
// }


// // compile expressions
// def compile_exp(a: Expr, env: Env) : String = a match {
//   case Num(i) => i"ldc $i"
//   case Var(s) => i"iload ${env(s)}"
//   case Aop(op, a1, a2) => 
//     compile_exp(a1, env) ++ compile_exp(a2, env) ++ compile_op(op)
//   case If(b, a1, a2) => {
//     val if_else = NewLabel("If_else")
//     val if_end = NewLabel("If_end")
//     compile_bexp(b, env, if_else) ++
//     compile_exp(a1, env) ++
//     i"goto $if_end" ++
//     l"$if_else" ++
//     compile_exp(a2, env) ++
//     l"$if_end"
//   }
//   case Call(name, args) => {
//     val is = "I" * args.length
//     args.map(a => compile_exp(a, env)).mkString ++
//     i"invokestatic XXX/XXX/$name($is)I"
//   }
//   case Sequence(a1, a2) => {
//     compile_exp(a1, env) ++ i"pop" ++ compile_exp(a2, env)
//   }
//   case Write(a1) => {
//     compile_exp(a1, env) ++
//     i"dup" ++
//     i"invokestatic XXX/XXX/write(I)V"
//   }
// }

// // compile boolean expressions
// def compile_bexp(b: BExp, env : Env, jmp: String) : String = b match {
//   case Bop("==", a1, a2) => 
//     compile_exp(a1, env) ++ compile_exp(a2, env) ++ i"if_icmpne $jmp"
//   case Bop("!=", a1, a2) => 
//     compile_exp(a1, env) ++ compile_exp(a2, env) ++ i"if_icmpeq $jmp"
//   case Bop("<", a1, a2) => 
//     compile_exp(a1, env) ++ compile_exp(a2, env) ++ i"if_icmpge $jmp"
//   case Bop("<=", a1, a2) => 
//     compile_exp(a1, env) ++ compile_exp(a2, env) ++ i"if_icmpgt $jmp"
// }

// // compile functions and declarations
// def compile_decl(d: Decl) : String = d match {
//   case Def(name, args, a) => { 
//     val env = args.zipWithIndex.toMap
//     val is = "I" * args.length
//     m".method public static $name($is)I" ++
//     m".limit locals ${args.length}" ++
//     m".limit stack ${1 + estimate_exp_stack(a)}" ++
//     l"${name}_Start" ++   
//     compile_exp(a, env) ++
//     i"ireturn" ++
//     m".end method\n"
//   }
//   case Main(a) => {
//     m".method public static main([Ljava/lang/String;)V" ++
//     m".limit locals 1" ++
//     m".limit stack ${1 + estimate_exp_stack(a)}" ++
//     compile_exp(a, Map()) ++
//     i"return" ++
//     m".end method\n"
//   }
// }

// // the main compilation function
// def compile(prog: List[Decl], class_name: String) : String = {
//   val instructions = prog.map(compile_decl).mkString
//   (start + instructions).replaceAllLiterally("XXX", class_name)
// }


// import ammonite.ops._

// def compile_to_file(prog: List[Decl], class_name: String) : Unit = 
//   write.over(pwd/"jfiles"/s"$class_name.j", compile(prog, class_name))  


// @main
// def main1(fname: String): Unit = {
//   val path = os.pwd / os.RelPath(fname)
//   val tmp = fname.stripSuffix("." ++ path.ext)
//   val class_name = tmp.substring(tmp.lastIndexOf("/")+1)  
//   compile_to_file(get_tree(fname),class_name)
// }

// @main
// def main2(fname: String) : Unit = {
//   val path = os.pwd / os.RelPath(fname)
//   val tmp = fname.stripSuffix("." ++ path.ext)
//   val class_name = tmp.substring(tmp.lastIndexOf("/")+1)  
// 	compile_to_file(get_tree(fname),class_name)
// 	println(s"Start of compilation")
// 	println(s"generated $class_name.j file")
// 	os.proc("java", "-jar", "bin/jasmin.jar", s"jfiles/$class_name.j").call()
// 	println(s"generated $class_name.class file")
// 	println(s"Run program")
// 	println()
// 	os.proc("java", s"${class_name}/${class_name}").call(stdout = os.Inherit)
// 	println()
//   println(s"done.")
  
// }


object Compiler {
}