package bigdata.audit.vo.alarm.notice

import bigdata.audit.vo.alarm.{AlarmConfig, AlarmScheduler}

case class MonitorAlarmConfig(ruleId: Number,
                             override val notices: Seq[Number],
                             override val scheduler: AlarmScheduler)
  extends AlarmConfig(notices, scheduler) {
  def toMap: java.util.Map[String, Any] = {
    import scala.collection.JavaConverters._

    Map[String, Any](
      "ruleId" -> ruleId.longValue().toString,
      "notices" -> notices.map(_.longValue()).asJava,
      "scheduler" -> scheduler.toMap
    ).asJava
  }
}
