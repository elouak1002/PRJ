package ast;

trait FLType
case object FLInt extends FLType
case object FLFloat extends FLType
case object FLBoolean extends FLType
case object FLUnit extends FLType
// case class FLOption(typ: FLType) extends FLType
case class FLFunc(args: Seq[FLType], typ: FLType) extends FLType

object FLType {

	def createFuncType(argsTyp: Seq[String], typ: String) : FLFunc = 
		FLFunc(argsTyp.map(createSingleType(_)), createSingleType(typ))

	// def createOptionType(typ: String): FLType = typ match {
	// 	case "Option[Int]" => FLOption(FLInt)
	// 	case "Option[Float]" => FLOption(FLFloat)
	// 	case "Option[Boolean]" => FLOption(FLBoolean)
	// 	case _ => FLOption(FLUnit)
	// }

	def createSingleType(typ: String): FLType = typ match {
		case "Int" => FLInt
		case "Float" => FLFloat
		case "Boolean" => FLBoolean
		case _ => FLUnit
	}
}