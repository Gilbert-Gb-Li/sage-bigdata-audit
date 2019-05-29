package bigdata.audit.service.audit.dsl.logic

import bigdata.audit.service.audit.dsl.ConditionResult
import bigdata.audit.vo.audit.dsl.config.logic.NotCondition

case class NotConditionService(config: NotCondition)
  extends BaseLogicConditionService(config) {

  override def logic(auditInfos: Seq[ConditionResult]): Boolean = {
    // not and
    auditInfos.exists(!_.result)
  }
}
