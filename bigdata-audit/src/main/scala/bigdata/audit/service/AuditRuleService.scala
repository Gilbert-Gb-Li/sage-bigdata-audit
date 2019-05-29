package bigdata.audit.service

import java.util.Date

import bigdata.audit.dao.AuditRuleDAO
import bigdata.audit.entity.AuditRule
import bigdata.audit.service.audit.AuditEngineService
import bigdata.audit.vo.audit.{AuditConfig, AuditInfo}
import bigdata.common.json.JsonMapper

import scala.concurrent.Future

class AuditRuleService extends JsonMapper {
  val ruleDAO = AuditRuleDAO()

  def process(rule: AuditRule, date: Date): AuditInfo = {
    val auditConfig = jsonMapper.readValue(rule.data, classOf[AuditConfig])
    val auditService = AuditEngineService(auditConfig)
    auditService.process(rule, date)
  }

  def getAll: Future[Seq[AuditRule]] = ruleDAO.getAll()
}
