package bigdata.audit.vo.audit.dsl

import bigdata.audit.vo.audit.dsl.compare._
import bigdata.audit.vo.audit.dsl.config._
import bigdata.audit.vo.audit.dsl.config.logic.{AndCondition, NotCondition, OrCondition}
import bigdata.audit.vo.audit.dsl.config.std.StdCondition
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.{As, Id}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(Array(
  new Type(value = classOf[AndCondition], name = "and"),
  new Type(value = classOf[OrCondition], name = "or"),
  new Type(value = classOf[NotCondition], name = "not"),
  new Type(value = classOf[StdCondition], name = "std"),
  new Type(value = classOf[GtCondition], name = ">"),
  new Type(value = classOf[GeCondition], name = ">="),
  new Type(value = classOf[LtCondition], name = "<"),
  new Type(value = classOf[LeCondition], name = "<="),
  new Type(value = classOf[EqCondition], name = "="),
  new Type(value = classOf[NeCondition], name = "!="),
  new Type(value = classOf[ContainsCondition], name = "contains")
))
abstract class Condition(val `type`: String) {
  def getIndicators: Seq[String]

}
