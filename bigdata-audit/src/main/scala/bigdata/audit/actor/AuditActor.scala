package bigdata.audit.actor

import java.util.Date

import akka.actor.{Actor, Props}
import bigdata.audit.service.AlarmRuleService
import bigdata.audit.utils.Constants
import bigdata.common.log4j2.Logging

class AuditActor extends Actor with Logging {

  override def receive: Receive = {
    case (Constants.messageDeal, ruleId: Long, date: Date) =>
      try {
        val service = AlarmRuleService()
        service.process(ruleId, date)
      } catch {
        case e: Exception =>
          logger.error(s"execute alarm rule($ruleId) fail", e)
      }
  }
}

object AuditActor {
  def props: Props = {
    Props(new AuditActor)
  }
}