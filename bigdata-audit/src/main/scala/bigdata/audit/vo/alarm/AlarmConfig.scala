package bigdata.audit.vo.alarm

import bigdata.audit.vo.alarm.notice.{MonitorAlarmConfig, ReportAlarmConfig}
import bigdata.common.json.JsonMapper

abstract class AlarmConfig(val notices: Seq[Number],
                           val scheduler: AlarmScheduler) {

}

object AlarmConfig extends JsonMapper {
  def apply(`type`: String, data: String): AlarmConfig = {
    `type` match {
      case "notice" | "report" =>
        jsonMapper.readValue(data, classOf[ReportAlarmConfig])
      case "monitor" =>
        jsonMapper.readValue(data, classOf[MonitorAlarmConfig])
      case _ =>
        null
    }
  }
}