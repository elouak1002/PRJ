package backend;

import ast._;
import ast.FLType._;

import ast.TypeAst._;
import ast.TypeAst;

// import cats.data.State;
import backend.JVMOpcode._;

object CompileExpr {

	def compileAexpOperator(op: String, typ: FLType): Opcode = (op,typ) match {
		case ("+",FLInt) => IADD
		case ("-",FLInt) => ISUB
		case ("/",FLInt) => IDIV
		case ("%",FLInt) => IREM
		case ("*",FLInt) => IMUL
		case ("+",FLDouble) => FADD
		case ("-",FLDouble) => FSUB
		case ("/",FLDouble) => FDIV
		case ("%",FLDouble) => FREM
		case ("*",FLDouble) => FMUL
	}

	def compileBexpOperator(op: String, typ: FLType, label: String): JVMBlock = {
		val firstOpcode: Opcode = typ match {
			case FLInt => ISUB
			case FLDouble => FCMPL
		}
		val secondOpcode: Opcode = op match {
			case "==" => IFNE(LabelRef(label))
			case "!=" => IFEQ(LabelRef(label))
			case "<" => IFGE(LabelRef(label))
			case "<=" => IFGT(LabelRef(label))
		}
		Seq(firstOpcode,secondOpcode)
	}

	def compileIf(expr: TypeAst.TypeExpr.TyIf) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyIf(bexp, b1, b2, typ,ifID) => {
			val label1 = JVMLabel("if_" + ifID.toString)
			val label2 = JVMLabel("else_" + ifID.toString)
			val jvmBexp = compileBexp(bexp, "else_" + ifID.toString)
			val jvmBlock1 = compileBlock(b1)
			val jvmBlock2 = compileBlock(b2)
			jvmBexp++:jvmBlock1++:Seq(GOTO(LabelRef("if_" + ifID.toString)),label2)++:jvmBlock2++:Seq(label1)
		}
	}

	def compileBexp(bexp: TypeAst.TypeBexp, label: String) : JVMBlock = bexp match {
		case TypeAst.TypeBexp.TyBop(op, aexp1, aexp2, exprTyp, typ) =>
			val jvmAexp1 = compileExpr(aexp1) 
			val jvmAexp2 = compileExpr(aexp2)
			val jvmBexp = compileBexpOperator(op,exprTyp,label)
			jvmAexp1++:jvmAexp2++:jvmBexp
	}
	
	def compileAop(expr: TypeAst.TypeExpr.TyAop) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyAop(op, aexp1, aexp2, typ) => {
			val jvmAexp1 = compileExpr(aexp1) 
			val jvmAexp2 = compileExpr(aexp2)
			val jvmOp = compileAexpOperator(op,typ)
			jvmAexp1++:jvmAexp2:+jvmOp
		}
	}


	def compileAssign(expr: TypeAst.TypeExpr.TyAssign) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyAssign(name, args, typ) => {
			compileBlock(args):+INVOKESTATIC(MethodCall(name, FLFunc(args.map(expr => getNodeType(expr)),typ)))
		}
	}

	def compileWrite(expr: TypeAst.TypeExpr.TyWrite) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyWrite(e, typ) => {
			val printMethod: Opcode = if (getNodeType(e)==FLInt) INVOKESTATIC(MethodCall("printInt", FLFunc(Seq(FLInt),FLUnit))) else INVOKESTATIC(MethodCall("printDouble", FLFunc(Seq(FLDouble),FLUnit)))
			compileExpr(e):+printMethod
		}
	}


	def compileValue(expr: TypeAst.TypeExpr.TyValue) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyValue(str, typ) => typ match {
			case FLInt => Seq(ILOAD(Address(nameToAddress(str))))
			case FLDouble => Seq(FLOAD(Address(nameToAddress(str))))
			case FLUnit => Seq()
		}	
	}

	def compileVal(expr: TypeAst.TypeExpr.TyVal) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyVal(name, expr, exprTyp, typ) => exprTyp match {
			case FLInt => compileExpr(expr):+ISTORE(Address(nameToAddress(name)))
			case FLDouble => compileExpr(expr):+FSTORE(Address(nameToAddress(name)))
			case FLUnit => compileExpr(expr)
		}
	}


	def compileExpr(expr: TypeAst.TypeExpr) : JVMBlock = expr match {
		case expr: TypeAst.TypeExpr.TyIf => compileIf(expr)
		case expr: TypeAst.TypeExpr.TyAssign => compileAssign(expr)
		case expr: TypeAst.TypeExpr.TyWrite => compileWrite(expr)
		case expr: TypeAst.TypeExpr.TyAop => compileAop(expr)
		case expr: TypeAst.TypeExpr.TyValue => compileValue(expr)
		case expr: TypeAst.TypeExpr.TyVal => compileVal(expr)
		case TypeAst.TypeExpr.TyDoubleExpr(num,typ) => Seq(LDC(Value(num.toString())))
		case TypeAst.TypeExpr.TyIntExpr(num,typ) => Seq(LDC(Value(num.toString())))
		case _ => Seq()
	}

	def compileBlock(block: TypeAst.TypeBlock) : JVMBlock = block match {
		case expr::xs => compileExpr(expr)++:compileDeclBody(xs)
		case Nil => List()
	}

	def compileDeclBody(block: TypeAst.TypeBlock) : JVMBlock = {
		compileBlock(block)
	}
}