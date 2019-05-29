package bigdata.common.dsl.analyst

object Mode extends Enumeration {
  type Mode = Value
  val Normal = Value(0)
  val StartString = Value(1)
  val StartEscape = Value(2)
  val StartStatic = Value(3)
  val StartBlank = Value(4)
}