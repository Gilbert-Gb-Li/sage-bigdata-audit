package bigdata.audit.service.audit.dsl.compare

import bigdata.audit.vo.audit.dsl.compare.LeCondition

case class LeConditionService(config: LeCondition)
  extends BaseCompareConditionService(config) {
  override def compare(d1: Double, d2: Double): Boolean = {
    d1 <= d2
  }
}
