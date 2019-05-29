package bigdata.audit.service

import java.util.Date

import bigdata.audit.AuditApp

object AlarmRuleServiceTest {
  def main(args: Array[String]): Unit = {
    AuditApp.main(Array[String]())
    AlarmRuleService().process(5L, new Date())
    System.exit(0)
  }
}
