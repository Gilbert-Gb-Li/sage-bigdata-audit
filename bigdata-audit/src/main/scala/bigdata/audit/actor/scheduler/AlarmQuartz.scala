package bigdata.audit.actor.scheduler

import bigdata.audit.AuditApp
import bigdata.audit.utils.Constants
import bigdata.common.log4j2.Logging
import org.quartz.{Job, JobExecutionContext}

class AlarmQuartz extends Job with Logging {
  override def execute(context: JobExecutionContext): Unit = {
    val detail = context.getJobDetail
    val ruleId = detail.getJobDataMap.getString("ruleId").toLong
    logger.info(s"trigger task:$ruleId date:${context.getFireTime}")
    AuditApp.auditActor ! (Constants.messageDeal, ruleId, context.getFireTime)
  }
}