package bigdata.audit.service.audit.dsl

import java.util.Date

import bigdata.audit.entity.{AuditRule, Indicator}
import bigdata.audit.service.audit.dsl.compare._
import bigdata.audit.service.audit.dsl.logic.{AndConditionService, NotConditionService, OrConditionService}
import bigdata.audit.service.audit.dsl.std.StdConditionService
import bigdata.audit.vo.audit.dsl.compare._
import bigdata.audit.vo.audit.dsl.config.logic.{AndCondition, NotCondition, OrCondition}
import bigdata.audit.vo.audit.dsl.config.std.StdCondition
import bigdata.audit.vo.audit.dsl.{Condition, DslAuditConfig}

abstract class ConditionService(config: Condition) {
  def process(rule: AuditRule, config: DslAuditConfig, date: Date,
              indicators:Map[String, Indicator]): ConditionResult

}

object ConditionService {
  def apply(config: Condition): ConditionService = {
    config match {
      case x: AndCondition =>
        AndConditionService(x)
      case x: OrCondition =>
        OrConditionService(x)
      case x: NotCondition =>
        NotConditionService(x)

      case x: StdCondition =>
        StdConditionService(x)

      case x: GtCondition =>
        GtConditionService(x)
      case x: GeCondition =>
        GeConditionService(x)

      case x: LtCondition =>
        LtConditionService(x)
      case x: LeCondition =>
        LeConditionService(x)


      case x: EqCondition =>
        EqConditionService(x)
      case x: NeCondition =>
        NeConditionService(x)
      case _ =>
        null
    }
  }
}