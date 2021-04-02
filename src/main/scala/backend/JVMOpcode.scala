package backend;

import ast._;
import ast.FLType._;

object JVMOpcode {

	val nameToAddress = (name: String) => name.drop(1)

	def typeToString(typ: FLType): String = typ match {
		case FLInt => "I"
		case FLDouble => "F" 
		case FLUnit => "V"
		case FLBoolean => "Z"
		case FLFunc(args,typ) => "(" + args.map(typeToString(_)).mkString + ")" + typeToString(typ)
	}

	type JVMProgHeader = Seq[Opcode]

	val header: String => JVMProgHeader = (x) => Seq(
		CLASS(Access("public"), ClassDecl(x)),
		SUPER(ClassCall("java/lang/Object"))
	)

	type JVMProgCore = Seq[JVMDecl]

	case class JVMProg(header: JVMProgHeader, core: JVMProgCore) {
		override def toString() = header.mkString("\n") + "\n\n" + core.mkString("\n")
	}

	type JVMBlock = Seq[Opcode]

	abstract class JVMDecl(name: String, locals: Int, stack: Int, typ: FLFunc, body: JVMBlock) {

		val method = ".method public static " + name + typeToString(typ)
		val localsStr  = "    .limit locals " + locals
		val stackStr = "    .limit stack " + stack
		val bodyStr = body.map(_.toString()).mkString("\n") 
		val end = ".end method"

		val newL = (str: String) => str + "\n"

		override def toString() = newL(method) + newL(localsStr) + newL(stackStr) + newL(bodyStr) + newL(end)
	}

	case class MethodDecl(name: String, locals: Int, stack: Int, typ: FLFunc, body: JVMBlock) extends JVMDecl(name, locals, stack, typ, body)
	case class MainDecl(body: JVMBlock) extends JVMDecl("main", 200, 200, FLFunc(Seq(), FLUnit), body) {
		override val method = ".method public static main([Ljava/lang/String;)V"
	}

	val printFula = (typ: FLType) => MethodDecl("printFula", 1,2,FLFunc(Seq(typ),FLUnit),Seq(
		GETSTATIC(ClassCall("java/lang/System/out"),FieldType("Ljava/io/PrintStream;")),
		if (typ==FLDouble) FLOAD(Address("0")) else ILOAD(Address("0")),
		INVOKEVIRTUAL(MethodCall("java/io/PrintStream/print",FLFunc(Seq(typ),FLUnit),"")),
		RETURN
	))

	val printlnFula = (typ: FLType) => MethodDecl("printlnFula", 1,2,FLFunc(Seq(typ),FLUnit),Seq(
		GETSTATIC(ClassCall("java/lang/System/out"),FieldType("Ljava/io/PrintStream;")),
		if (typ==FLDouble) FLOAD(Address("0")) else ILOAD(Address("0")),
		INVOKEVIRTUAL(MethodCall("java/io/PrintStream/println",FLFunc(Seq(typ),FLUnit),"")),
		RETURN
	))

	val utils: JVMProgCore = Seq(FLInt, FLDouble, FLBoolean).flatMap(typ => Seq(printFula(typ),printlnFula(typ)))

	trait Opcode

	abstract class UnaryOpcode extends Opcode {
		override def toString() = "    " + this.getClass.getSimpleName.toLowerCase().replace("$","")
	}

	case object IADD extends UnaryOpcode 
	case object ISUB extends UnaryOpcode
	case object IDIV extends UnaryOpcode
	case object IREM extends UnaryOpcode
	case object IMUL extends UnaryOpcode
	case object FADD extends UnaryOpcode
	case object FSUB extends UnaryOpcode
	case object FDIV extends UnaryOpcode
	case object FREM extends UnaryOpcode
	case object FMUL extends UnaryOpcode
	case object FCMPL extends UnaryOpcode
	case object IRETURN extends UnaryOpcode
	case object FRETURN extends UnaryOpcode
	case object RETURN extends UnaryOpcode

	abstract class BinaryOpcode(operand: Seq[Operand]) extends Opcode {
		override def toString() = "    " + this.getClass.getSimpleName.toLowerCase().replace("$","") + " " + operand.mkString(" ")
	}

	case class ISTORE(operand: Address) extends BinaryOpcode(Seq(operand))
	case class ILOAD(operand: Address) extends BinaryOpcode(Seq(operand))
	case class FSTORE(operand: Address) extends BinaryOpcode(Seq(operand))
	case class FLOAD(operand: Address) extends BinaryOpcode(Seq(operand))
	case class LDC(operand: Value) extends BinaryOpcode(Seq(operand))
	case class GOTO(operand: LabelRef) extends BinaryOpcode(Seq(operand))
	case class IFEQ(operand: LabelRef) extends BinaryOpcode(Seq(operand))
	case class IFNE(operand: LabelRef) extends BinaryOpcode(Seq(operand))
	case class IFGT(operand: LabelRef) extends BinaryOpcode(Seq(operand))
	case class IFGE(operand: LabelRef) extends BinaryOpcode(Seq(operand))
	case class INVOKESTATIC(operand: MethodCall) extends BinaryOpcode(Seq(operand))
	case class INVOKEVIRTUAL(operand: MethodCall) extends BinaryOpcode(Seq(operand))
	case class GETSTATIC(className: ClassCall, field: FieldType) extends BinaryOpcode(Seq(className,field))
	case class CLASS(access: Access, className: ClassDecl=ClassDecl("XXXX")) extends BinaryOpcode(Seq(access,className)) {
		override def toString() = ".class " + access.toString() + " " + className.toString()
	}
	case class SUPER(className : ClassCall) extends BinaryOpcode(Seq(className)) {
		override def toString() = ".super " + className.toString()
	}

	case class JVMLabel(name: String) extends Opcode {
		override def toString() = name + ":"
	}
	
	abstract class Operand(name: String) {
		override def toString() = name
	}

	case class Address(x: String) extends Operand(x)
	case class Value(str: String) extends Operand(str)
	case class LabelRef(name: String) extends Operand(name)
	case class ClassCall(name: String) extends Operand(name)
	case class FieldType(field: String) extends Operand(field)
	case class Access(name: String) extends Operand(name)
	case class ClassDecl(name: String) extends Operand(name) {
		override def toString() = name + "." + name
	}

	case class MethodCall(name: String, typ: FLFunc, className: String="XXXX") extends Operand(name) {
		override def toString() = if (className != "") (className + ".")*2 + name + typeToString(typ) else name + typeToString(typ)
	}

}