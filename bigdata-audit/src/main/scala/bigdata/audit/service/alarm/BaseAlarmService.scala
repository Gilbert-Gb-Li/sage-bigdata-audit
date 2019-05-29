package bigdata.audit.service.alarm

import java.util

import bigdata.audit.dao.{AuditRuleDAO, NoticeDAO}
import bigdata.audit.service.AuditRuleService
import bigdata.audit.service.notice.NoticeService
import bigdata.audit.vo.alarm.AlarmConfig
import bigdata.audit.vo.notice.NoticeConfig
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

abstract class BaseAlarmService(override val config: AlarmConfig)
  extends AlarmService(config) with JsonMapper with Logging {
  protected val auditRuleDAO = AuditRuleDAO()
  protected val auditRuleService = new AuditRuleService()


  def dealNotice(noticeDAO: NoticeDAO, nid: Long, title: String, model: util.Map[String, Any]): Boolean = {
    val notice = Await.result(noticeDAO.getById(nid), Duration.Inf) match {
      case Some(x) =>
        x
      case _ =>
        return false
    }
    val noticeConfig = NoticeConfig(notice.`type`, notice.data)
    val noticeService = NoticeService(noticeConfig)
    val content = genContent(notice.`type`, model)
    noticeService.process(title, content)
    true
  }

  def genContent(`type`: String, model: util.Map[String, Any]): String
}