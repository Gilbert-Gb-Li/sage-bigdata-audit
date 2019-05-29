package bigdata.audit.vo.audit.dsl.config.logic

import bigdata.audit.vo.audit.dsl.Condition

case class NotCondition(override val conditions: Seq[Condition])
  extends BaseLogicCondition("not", conditions) {

}
