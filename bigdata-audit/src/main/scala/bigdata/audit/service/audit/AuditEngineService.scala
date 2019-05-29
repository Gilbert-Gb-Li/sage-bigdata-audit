package bigdata.audit.service.audit

import java.util.Date

import bigdata.audit.entity.AuditRule
import bigdata.audit.service.audit.curve.CurveAuditEngineService
import bigdata.audit.service.audit.dsl.DslAuditService
import bigdata.audit.vo.audit.{AuditConfig, AuditInfo}
import bigdata.audit.vo.audit.curve.CurveAuditConfig
import bigdata.audit.vo.audit.dsl.DslAuditConfig

abstract class AuditEngineService {
  def process(rule: AuditRule, date: Date): AuditInfo
}

object AuditEngineService {
  def apply(config: AuditConfig): AuditEngineService = {

    config match {
      case x: CurveAuditConfig =>
        CurveAuditEngineService(x)
      case x: DslAuditConfig =>
        DslAuditService(x)
      case _ =>
        // TODO
        null
    }
  }
}
