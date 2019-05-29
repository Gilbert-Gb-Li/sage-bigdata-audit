package bigdata.audit.service.audit.dsl.compare

import bigdata.audit.vo.audit.dsl.compare.LtCondition

case class LtConditionService(config: LtCondition)
  extends BaseCompareConditionService(config) {
  override def compare(d1: Double, d2: Double): Boolean = {
    d1 < d2
  }

}
