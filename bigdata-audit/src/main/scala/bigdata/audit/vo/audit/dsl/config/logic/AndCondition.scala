package bigdata.audit.vo.audit.dsl.config.logic

import bigdata.audit.vo.audit.dsl.Condition

case class AndCondition(override val conditions: Seq[Condition])
  extends BaseLogicCondition("and", conditions) {

}
