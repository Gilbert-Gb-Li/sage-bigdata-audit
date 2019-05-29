package bigdata.audit.service.audit.dsl

import java.util.{Date, UUID}

import bigdata.audit.dao.IndicatorDAO
import bigdata.audit.entity.{AuditRule, Indicator}
import bigdata.audit.service.audit.AuditEngineService
import bigdata.audit.vo.audit.AuditInfo
import bigdata.audit.vo.audit.dsl.DslAuditConfig
import bigdata.common.json.JsonMapper

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class DslAuditService(config: DslAuditConfig) extends AuditEngineService with JsonMapper {
  val conditionService = ConditionService(config.condition)
  val indicatorDAO = IndicatorDAO()

  override def process(rule: AuditRule, date: Date): AuditInfo = {
    val indicatorIds = config.condition.getIndicators
    val indicators: Map[String, Indicator] = indicatorIds.map(id => {
      val indicator = Await.result(indicatorDAO.getById(id), Duration.Inf) match {
        case Some(x) =>
          x
        case _ =>
          null
      }
      (id, indicator)
    }).toMap


    val conditionResult = conditionService.process(rule, config, date, indicators)

    val info = AuditInfo(UUID.randomUUID().toString, rule.name, conditionResult.content,
      conditionResult.data, rule.id.get, rule.appId, conditionResult.result,
      conditionResult.detail, new Date())
    info
  }
}
