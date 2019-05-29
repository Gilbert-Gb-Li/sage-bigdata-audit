package bigdata.audit.service.audit.dsl.logic

import bigdata.audit.service.audit.dsl.ConditionResult
import bigdata.audit.vo.audit.dsl.config.logic.AndCondition

case class AndConditionService(config: AndCondition)
  extends BaseLogicConditionService(config) {

  override def logic(auditInfos: Seq[ConditionResult]): Boolean = {
    auditInfos.forall(info => {
      info.result
    })
  }
}
