package bigdata.audit.service.audit.dsl.logic

import java.util.Date

import bigdata.audit.entity.{AuditRule, Indicator}
import bigdata.audit.service.audit.dsl.{ConditionResult, ConditionService}
import bigdata.audit.vo.audit.dsl.DslAuditConfig
import bigdata.audit.vo.audit.dsl.config.logic.BaseLogicCondition
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging

abstract class BaseLogicConditionService(config: BaseLogicCondition)
  extends ConditionService(config) with JsonMapper with Logging {
  protected val services: Seq[ConditionService] = config.conditions.map(condition => {
    ConditionService(condition)
  })

  def logic(auditInfos: Seq[ConditionResult]): Boolean

  def typeFormat: String = config.`type`

  def process(rule: AuditRule, dslAlarmConfig: DslAuditConfig, curDate: Date,
              indicators: Map[String, Indicator]): ConditionResult = {
    val auditInfos: Seq[ConditionResult] = services.map(service => {
      service.process(rule, dslAlarmConfig, curDate, indicators)
    })
    val res = logic(auditInfos)

    val content: String = s"$typeFormat\n" + auditInfos.map(_.content).mkString("\n")
    val data = auditInfos.map(info => {
      info.data
    }).find(item => {
      item != null && item.nonEmpty
    }) match {
      case Some(x) =>
        x
      case _ =>
        ""
    }

    val detail = auditInfos.filter(!_.result).map(_.detail).mkString("\n")
    ConditionResult(res, content, data, detail)
  }
}