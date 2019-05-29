package bigdata.common.dsl.analyst.syntax

object SyntaxMode extends Enumeration {
  type SyntaxMode = Value
  val Normal = Value(1)
  val Param = Value(2)
  val StartExpression = Value(3)
  val EmptyExpression = Value(4)
  val Over = Value(5)
}
