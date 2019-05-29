package bigdata.audit.service.alarm

import java.util.Date

import bigdata.audit.entity.AlarmRule
import bigdata.audit.service.alarm.monitor.MonitorAlarmService
import bigdata.audit.service.alarm.report.ReportAlarmService
import bigdata.audit.vo.alarm.AlarmConfig
import bigdata.audit.vo.alarm.notice.{MonitorAlarmConfig, ReportAlarmConfig}

abstract class AlarmService(val config: AlarmConfig) {
  def process(rule: AlarmRule, fireDate: Date, bizDate: Date): Boolean
}

object AlarmService {
  def apply(config: AlarmConfig): AlarmService = {
    config match {
      case x: ReportAlarmConfig =>
        ReportAlarmService(x)
      case x: MonitorAlarmConfig =>
        MonitorAlarmService(x)
      case _ =>
        null
    }
  }
}