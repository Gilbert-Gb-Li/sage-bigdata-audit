package bigdata.audit.service.alarm.report

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import bigdata.audit.dao.NoticeDAO
import bigdata.audit.entity.{AlarmRule, AuditRule}
import bigdata.audit.service.alarm.BaseAlarmService
import bigdata.audit.service.audit.FtlAlarmContent
import bigdata.audit.vo.alarm.notice.ReportAlarmConfig
import bigdata.audit.vo.audit.AuditInfo
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class ReportAlarmService(override val config: ReportAlarmConfig)
  extends BaseAlarmService(config) with JsonMapper with Logging {


  def genContent(`type`: String, model: util.Map[String, Any]): String = {
    try {
      `type` match {
        case "mail" =>
          FtlAlarmContent.processNoticeMail(model)
        case "ding2" =>
          FtlAlarmContent.processNoticeDing2(model)
        case _ =>
          ""
      }
    } catch {
      case e: Exception =>
        logger.error("gen report content fail", e)
        ""
    }
  }

  override def process(rule: AlarmRule, fireDate: Date, bizDate: Date): Boolean = {
    // audit
    val ruleIds: Seq[Long] = config.groups.flatMap(_.rules.map(_.longValue()))
    val rules: Seq[AuditRule] = ruleIds.map(ruleId => {
      Await.result(auditRuleDAO.getById(ruleId), Duration.Inf)
    }).filter(_.isDefined).map(_.get)
    val auditInfos: Seq[AuditInfo] = rules.map(rule => {
      auditRuleService.process(rule, bizDate)
    })
    // mail
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    val dstDate = sdf.format(bizDate)
    val title = s"${rule.name}日报[${dstDate}]"
    import scala.collection.JavaConverters._
    val model: util.Map[String, Any] = Map[String, Any]("items" -> auditInfos.map(_.toMap()).asJava,
      "config" -> config.toMap,
      "rule" -> rule.toMap,
      "date" -> dstDate,
      "fireDate" -> sdf.format(fireDate)
    ).asJava


    val noticeDAO = NoticeDAO()
    val ids = config.notices.map(_.longValue())
    ids.foreach(nid => {
      dealNotice(noticeDAO, nid, title, model)
    })
    logger.info(s"had process alarm rule:${rule.id.getOrElse("")}")
    true
  }
}
