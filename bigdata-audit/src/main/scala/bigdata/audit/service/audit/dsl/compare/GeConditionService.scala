package bigdata.audit.service.audit.dsl.compare

import bigdata.audit.vo.audit.dsl.compare.GeCondition
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging

case class GeConditionService(config: GeCondition)
  extends BaseCompareConditionService(config) with JsonMapper with Logging {
  override def compare(d1: Double, d2: Double): Boolean = {
    d1 >= d2
  }

}
