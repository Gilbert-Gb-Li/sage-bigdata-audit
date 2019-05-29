package bigdata.audit.service.alarm.monitor

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import bigdata.audit.dao.NoticeDAO
import bigdata.audit.entity.AlarmRule
import bigdata.audit.service.alarm.BaseAlarmService
import bigdata.audit.service.audit.FtlAlarmContent
import bigdata.audit.vo.alarm.notice.MonitorAlarmConfig

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class MonitorAlarmService(override val config: MonitorAlarmConfig)
  extends BaseAlarmService(config) {


  override def process(rule: AlarmRule, fireDate: Date, bizDate: Date): Boolean = {
    // audit
    val auditRuleId = config.ruleId.longValue()
    val auditRule = Await.result(auditRuleDAO.getById(auditRuleId), Duration.Inf).orNull
    val auditInfo = auditRuleService.process(auditRule, bizDate)
    if (!auditInfo.result) {
      val title = "告警：" + rule.name
      import scala.collection.JavaConverters._
      val sdf = new SimpleDateFormat("yyyy-MM-dd")
      val model: util.Map[String, Any] = Map[String, Any]("info" -> auditInfo.toMap(),
        "config" -> config.toMap,
        "rule" -> rule.toMap,
        "date" -> sdf.format(bizDate),
        "fireDate" -> sdf.format(fireDate)
      ).asJava


      val noticeDAO = NoticeDAO()
      val ids = config.notices.map(_.longValue())
      ids.foreach(nid => {
        dealNotice(noticeDAO, nid, title, model)
      })
      logger.info(s"had process alarm rule:${rule.id.getOrElse("")}")
    }


    true
  }

  override def genContent(`type`: String, model: util.Map[String, Any]): String = {
    try {
      `type` match {
        case "mail" =>
          FtlAlarmContent.processMonitorMail(model)
        case "ding2" =>
          FtlAlarmContent.processMonitorDing2(model)
        case _ =>
          ""
      }
    } catch {
      case e: Exception =>
        logger.error("gen report content fail", e)
        ""
    }
  }
}
