package bigdata.audit.vo.audit.dsl.config.logic

import bigdata.audit.vo.audit.dsl.Condition

case class OrCondition(override val conditions: Seq[Condition])
  extends BaseLogicCondition("or", conditions) {

}
