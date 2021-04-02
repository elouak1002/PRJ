package backend;

import ast._;
import ast.FLType._;

import ast.TypeAst._;
import ast.TypeAst;

// import cats.data.State;
import backend.JVMOpcode._;

object CompileExpr {

	def compileAexpOperator(op: String, typ: FLType): JVMBlock = (op,typ) match {
		case ("+",FLInt) => Seq(IADD)
		case ("-",FLInt) => Seq(ISUB)
		case ("/",FLInt) => Seq(IDIV)
		case ("%",FLInt) => Seq(IREM)
		case ("*",FLInt) => Seq(IMUL)
		case ("+",FLDouble) => Seq(FADD)
		case ("-",FLDouble) => Seq(FSUB)
		case ("/",FLDouble) => Seq(FDIV)
		case ("%",FLDouble) => Seq(FREM)
		case ("*",FLDouble) => Seq(FMUL)
		case (_,_) => Seq()
	}

	def compileBexpOperator(op: String, typ: FLType, label: String): JVMBlock = {
		val firstOpcode: Opcode = typ match {
			case FLInt|FLBoolean => ISUB
			case FLDouble => FCMPL
		}

		val labelFalseName = "bexp_"+label+"_false"
		val labelFalse = JVMLabel(labelFalseName)
		val labelRefFalse = LabelRef(labelFalseName)
		
		val labelEndName = "bexp_"+label+"_end"
		val labelEnd = JVMLabel(labelEndName)
		val labelRefEnd = LabelRef(labelEndName)

		val secondOpcode: Opcode = op match {
			case "==" => IFNE(labelRefFalse)
			case "!=" => IFEQ(labelRefFalse)
			case "<" => IFGE(labelRefFalse)
			case "<=" => IFGT(labelRefFalse)
		}

		val evalBexp: JVMBlock = Seq(LDC(Value("1")),GOTO(labelRefEnd),labelFalse,LDC(Value("0")),labelEnd)
		Seq(firstOpcode,secondOpcode)++:evalBexp
	}

	def compileIf(expr: TypeAst.TypeExpr.TyIf) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyIf(bexp, b1, b2, typ,labelID) => {
			
			val labelFalseName = "bexp_"+labelID.toString()+"_false"
			val labelFalse = JVMLabel(labelFalseName)
			val labelRefFalse = LabelRef(labelFalseName)
			
			val labelEndName = "bexp_"+labelID.toString()+"_end"
			val labelEnd = JVMLabel(labelEndName)
			val labelRefEnd = LabelRef(labelEndName)

			val jvmBexp = compileBexp(bexp)

			val jvmBlock1 = compileBlock(b1)
			val jvmBlock2 = compileBlock(b2)

			jvmBexp++:Seq(IFEQ(labelRefFalse))++:jvmBlock1++:Seq(GOTO(labelRefEnd),labelFalse)++:jvmBlock2++:Seq(labelEnd)
		}
	}

	def compileBexp(bexp: TypeAst.TypeExpr.TypeBexp) : JVMBlock = bexp match {
		case TypeAst.TypeExpr.TypeBexp.TyBop(op, aexp1, aexp2, exprTyp, typ,labelID) =>
			val jvmAexp1 = compileExpr(aexp1) 
			val jvmAexp2 = compileExpr(aexp2)
			val jvmBexp = compileBexpOperator(op,exprTyp,labelID.toString())
			jvmAexp1++:jvmAexp2++:jvmBexp
	}
	
	def compileAop(expr: TypeAst.TypeExpr.TyAop) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyAop(op, aexp1, aexp2, typ) => {
			val jvmAexp1 = compileExpr(aexp1) 
			val jvmAexp2 = compileExpr(aexp2)
			val jvmOp = compileAexpOperator(op,typ)
			jvmAexp1++:jvmAexp2++:jvmOp
		}
	}


	def compileAssign(expr: TypeAst.TypeExpr.TyAssign) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyAssign(name, args, typ) => {
			compileBlock(args):+INVOKESTATIC(MethodCall(name, FLFunc(args.map(expr => getNodeType(expr)),typ)))
		}
	}

	def compileWrite(expr: TypeAst.TypeExpr.TyWrite) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyWrite(e, typ) => getNodeType(e) match {
			case FLUnit|FLFunc(_,_) =>  Seq()
			case _ => compileExpr(e):+INVOKESTATIC(MethodCall("printlnFula", FLFunc(Seq(getNodeType(e)),FLUnit)))
		}
	}


	def compileValue(expr: TypeAst.TypeExpr.TyValue) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyValue(str, typ) => typ match {
			case FLInt|FLBoolean => Seq(ILOAD(Address(nameToAddress(str))))
			case FLDouble => Seq(FLOAD(Address(nameToAddress(str))))
			case FLUnit => Seq()
		}	
	}

	def compileVal(expr: TypeAst.TypeExpr.TyVal) : JVMBlock = expr match {
		case TypeAst.TypeExpr.TyVal(name, expr, exprTyp, typ) => exprTyp match {
			case FLInt|FLBoolean => compileExpr(expr):+ISTORE(Address(nameToAddress(name)))
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
		case expr: TypeAst.TypeExpr.TypeBexp.TyBop => compileBexp(expr)
		case TypeAst.TypeExpr.TyDoubleExpr(num,typ) => Seq(LDC(Value(num.toString())))
		case TypeAst.TypeExpr.TyIntExpr(num,typ) => Seq(LDC(Value(num.toString())))
		case TypeAst.TypeExpr.TyBooleanExpr(bool,typ) => if (bool) Seq(LDC(Value("1"))) else Seq(LDC(Value("0")))
	}

	def compileBlock(block: TypeAst.TypeBlock) : JVMBlock = block match {
		case expr::xs => compileExpr(expr)++:compileDeclBody(xs)
		case Nil => List()
	}

	def compileDeclBody(block: TypeAst.TypeBlock) : JVMBlock = {
		compileBlock(block)
	}
}