package bigdata.audit.actor.scheduler

import akka.actor.{Actor, Cancellable, Props}
import bigdata.audit.AuditApp._
import bigdata.audit.entity.AlarmRule
import bigdata.audit.service.AlarmRuleService
import bigdata.audit.utils.Constants
import bigdata.audit.vo.alarm.AlarmConfig
import bigdata.common.log4j2.Logging
import org.quartz._
import org.quartz.impl.StdSchedulerFactory

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * 任务调度Actor： 发送消息给 auditActor, 触发审计任务
  */
class SchedulerActor extends Actor with Logging {
  private val quartz = StdSchedulerFactory.getDefaultScheduler
  private val ruleCronMap = mutable.Map[String, String]()
  private var scheduler: Cancellable = _

  override def preStart(): Unit = {
    super.preStart()
    this.quartz.start()
    // 每3分钟更新一下数据库
    val initialDelay = FiniteDuration(1, "ms")
    val interval = FiniteDuration(1, "min")
    scheduler = system.scheduler.schedule(initialDelay, interval,
      self, Constants.messageDeal)
  }

  def register(rule: AlarmRule): Boolean = {
    logger.debug(s"start register scheduler job(${rule.id.getOrElse("")})")
    val l1 = System.currentTimeMillis()
    val alarmConfig = AlarmConfig(rule.`type`, rule.data)
    if (alarmConfig == null ||
      alarmConfig.scheduler == null ||
      alarmConfig.scheduler.cron == null) {
      logger.error(s"rule:${rule.id.get} config error")
      return false
    }
    val key = s"alarm_rule_id_${rule.id.get}"
    val group = "alarm_rule"
    val lastCron: String = ruleCronMap.get(key) match {
      case Some(x) =>
        x
      case None =>
        null
    }
    if (lastCron == null || lastCron != alarmConfig.scheduler.cron) {
      this.quartz.deleteJob(new JobKey(key, group))

      val job = JobBuilder.newJob(classOf[AlarmQuartz])
        .withIdentity(key, group)
        .usingJobData("ruleId", rule.id.get.toString) //定义属性
        .build()

      val trigger = TriggerBuilder.newTrigger()
        .withIdentity(key)
        .withSchedule(CronScheduleBuilder.cronSchedule(alarmConfig.scheduler.cron))
        .build()

      this.quartz.scheduleJob(job, trigger)
    }
    val use = System.currentTimeMillis() - l1
    logger.debug(s"finished register scheduler job(${rule.id.getOrElse("")}), use time:${use}")
    true
  }

  override def receive: Receive = {
    case Constants.messageDeal =>
      val service = AlarmRuleService()
      val rules = Await.result(service.getAll, Duration.Inf)
      rules.foreach(register)

  }

  override def postStop(): Unit = {
    super.postStop()
    try {
      this.scheduler.cancel()
    }
    try {
      this.quartz.shutdown()
    }
  }

}


object SchedulerActor {
  def props: Props = {
    Props(new SchedulerActor)
  }
}