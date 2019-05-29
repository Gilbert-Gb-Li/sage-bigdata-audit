package bigdata.audit.service.audit.dsl.compare

import java.util.{Calendar, Date}

import bigdata.audit.entity.{AuditRule, Indicator}
import bigdata.audit.service.fetch.FetchService
import bigdata.audit.vo.audit.dsl.compare.CompareIndicator
import bigdata.audit.vo.audit.dsl.{Condition, DslAuditConfig}
import bigdata.audit.vo.fetch.FetchConfig
import bigdata.common.json.JsonMapper

case class CompareIndicatorService(config: CompareIndicator) extends JsonMapper {
  def process(rule: AuditRule, dslAlarmConfig: DslAuditConfig,
              condition: Condition, curDate: Date,
              indicators: Map[String, Indicator]): (Double, Double) = {
    if (config.indicator == null || config.indicator.isEmpty) {
      return (config.value, config.value)
    }
    // 获取指标值
    val value = fetch(rule, dslAlarmConfig, condition, curDate, indicators)
    config.`type` match {
      case "+" =>
        (value, value + config.value)
      case "-" =>
        (value, value - config.value)
      case "/" | "\\" =>
        (value, value / config.value)
      case _ =>
        (value, value * config.value)
    }
  }

  def fetch(rule: AuditRule, dslAlarmConfig: DslAuditConfig,
            condition: Condition, curDate: Date, indicators: Map[String, Indicator]): Double = {
    val indicator = indicators.get(config.indicator) match {
      case Some(x) =>
        x
      case _ =>
        return 0
    }
    val fetchConfig = jsonMapper.readValue(indicator.data, classOf[FetchConfig])
    val fetchService = FetchService(fetchConfig)

    val calendar = Calendar.getInstance()

    calendar.setTime(curDate)
    indicator.unit match {
      case "week" | "w" =>
        calendar.set(Calendar.DAY_OF_WEEK, indicator.param)
      case "month" | "m" =>
        calendar.set(Calendar.DAY_OF_MONTH, indicator.param)
      case "year" | "y" =>
        calendar.set(Calendar.DAY_OF_YEAR, indicator.param)
      case "day" | "d" | _ =>
    }
    val dstDate = calendar.getTime

    try {
      fetchService.process(dstDate)
    } catch {
      case e: Exception =>
        if (e.getMessage.contains("IndexNotFoundException")) {
          0
        } else {
          throw new Exception("fetch auditInfos fail", e)
        }
    }
  }
}
