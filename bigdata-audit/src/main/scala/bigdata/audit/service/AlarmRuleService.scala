package bigdata.audit.service

import java.util.{Calendar, Date}

import bigdata.audit.dao.AlarmRuleDAO
import bigdata.audit.entity.AlarmRule
import bigdata.audit.service.alarm.AlarmService
import bigdata.audit.vo.alarm.AlarmConfig
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class AlarmRuleService() extends JsonMapper with Logging {
  val ruleDAO = AlarmRuleDAO()

  def process(rule: AlarmRule, fireDate: Date): Boolean = {
    val alarmConfig = AlarmConfig(rule.`type`, rule.data)
    val calendar = java.util.Calendar.getInstance()
    calendar.setTime(fireDate)
    alarmConfig.scheduler.unit match {
      case "h" | "hour" =>
        calendar.add(Calendar.HOUR, alarmConfig.scheduler.offset)
      case "d" | "day" =>
        calendar.add(Calendar.DAY_OF_YEAR, alarmConfig.scheduler.offset)
      case "w" | "week" =>
        calendar.add(Calendar.WEEK_OF_YEAR, alarmConfig.scheduler.offset)
      case "m" | "month" =>
        calendar.add(Calendar.MONTH, alarmConfig.scheduler.offset)
    }
    val alarmService = AlarmService(alarmConfig)
    alarmService.process(rule, fireDate, calendar.getTime)
  }

  def process(ruleId: Long, date: Date): Boolean = {
    logger.info(s"start process alarm rule:$ruleId at $date")
    val rule = Await.result(ruleDAO.getById(ruleId), Duration.Inf) match {
      case Some(x) =>
        logger.debug(s"alarm rule:${toJson(x)}")
        x
      case _ =>
        return false
    }
    process(rule, date)
  }


  def getAll: Future[Seq[AlarmRule]] = ruleDAO.getAll
}
