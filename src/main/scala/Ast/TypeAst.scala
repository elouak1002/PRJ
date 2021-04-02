package ast;

object TypeAst {

	sealed trait TypeAstNode

	// A sequence of expression semi-column separated is a block
	type TypeBlock = Seq[TypeExpr]
	
	sealed trait TypeExpr extends TypeAstNode
	object TypeExpr {
		case class TyIf(a: TypeBexp, e1: TypeBlock, e2: TypeBlock, typed: FLType, labelID: Int=(-1)) extends TypeExpr
		case class TyAssign(name: String, args: Seq[TypeExpr], typed: FLType) extends TypeExpr
		case class TyValue(s: String, typed: FLType) extends TypeExpr
		case class TyWrite(e: TypeExpr, typed: FLType=FLUnit) extends TypeExpr
		case class TyIntExpr(i: Int, typed: FLType=FLInt) extends TypeExpr
		case class TyDoubleExpr(d: Double, typed: FLType=FLDouble) extends TypeExpr
		case class TyBooleanExpr(b: Boolean, typed: FLType=FLBoolean) extends TypeExpr
		case class TyAop(o: String, a1: TypeExpr, a2: TypeExpr, typed: FLType) extends TypeExpr
		case class TyVal(name: String, e: TypeExpr, exprTyp: FLType, typed: FLType=FLUnit) extends TypeExpr


		// A boolean expression
		sealed trait TypeBexp extends TypeExpr
		object TypeBexp {
			case class TyBop(o: String, a1: TypeExpr, a2: TypeExpr, exprTyp: FLType, typed: FLType=FLBoolean,labelID: Int=(-1)) extends TypeBexp
		}
	}

	sealed trait TypeDecl extends TypeAstNode
	object TypeDecl	{
		case class TyDef(name: String, args: Seq[(String,String)], body: TypeBlock, typed: FLFunc) extends TypeDecl
		case class TyMain(name: String="main", body: TypeBlock, typed: FLFunc=FLFunc(Seq(),FLUnit)) extends TypeDecl
	}

	type TypeProg = Seq[TypeDecl]


	def getNodeType(node: TypeAstNode) : FLType = node match {
		case TypeExpr.TypeBexp.TyBop(_,_,_,_,typ,_) => typ
		case TypeExpr.TyIf(_,_,_,typ,_) => typ
		case TypeExpr.TyAssign(_,_,typ) => typ
		case TypeExpr.TyValue(_,typ) => typ
		case TypeExpr.TyWrite(_,typ) => typ
		case TypeExpr.TyIntExpr(_,typ) => typ
		case TypeExpr.TyDoubleExpr(_,typ) => typ
		case TypeExpr.TyBooleanExpr(_,typ) => typ
		case TypeExpr.TyAop(_,_,_,typ) => typ
		case TypeExpr.TyVal(_,_,_,typ) => typ
		case TypeDecl.TyDef(_,_,_,typ) => typ
		case TypeDecl.TyMain(_,_,typ) => typ
	}

	def getBlockType(block: TypeBlock) : FLType = {
		try {getNodeType(block.last) }
		catch {case e: Exception => FLUnit }
	}
}