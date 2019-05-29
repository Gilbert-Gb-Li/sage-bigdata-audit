package bigdata.audit.service.audit.dsl.compare

import java.text.NumberFormat
import java.util.Date

import bigdata.audit.entity.{AuditRule, Indicator}
import bigdata.audit.service.audit.dsl.{ConditionResult, ConditionService}
import bigdata.audit.vo.audit.dsl.DslAuditConfig
import bigdata.audit.vo.audit.dsl.compare.{CompareCondition, CompareIndicator}
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging

abstract class BaseCompareConditionService(config: CompareCondition)
  extends ConditionService(config) with JsonMapper with Logging {
  protected val service1 = CompareIndicatorService(config.param1)
  protected val service2 = CompareIndicatorService(config.param2)
  protected val nf: NumberFormat = NumberFormat.getInstance()


  def process(rule: AuditRule, dslAlarmConfig: DslAuditConfig, curDate: Date,
              indicators: Map[String, Indicator]): ConditionResult = {
    val v1 = service1.process(rule, dslAlarmConfig, config, curDate, indicators)
    val v2 = service2.process(rule, dslAlarmConfig, config, curDate, indicators)
    val res = compare(v1._1, v2._1)
    val content: String = if (config.param1.indicator != null) {
      if (config.param2.indicator != null) {
        s"$res, ${format(v1, config.param1)} $typeFormat ${format(v2, config.param2)} "
      } else {
        s"$res, ${format(v1, config.param1)} $typeFormat ${config.param2.value} "
      }
    } else {
      if (config.param2.indicator != null) {
        s"$res, ${config.param1.value} $typeFormat ${format(v2, config.param1)}"
      } else {
        s"$res, ${v1._2} $typeFormat ${v2._2}"
      }
    }
    val data = if (dslAlarmConfig.indicator == config.param1.indicator) {
      nf.format(new java.math.BigDecimal(String.valueOf(v1._1)))
    } else if (dslAlarmConfig.indicator == config.param1.indicator) {
      nf.format(new java.math.BigDecimal(String.valueOf(v2._1)))
    } else {
      ""
    }
    val detail = res + typeTipsFormat
    if (logger.isDebugEnabled()) {
      logger.debug(s"condition(${toJson(config)}) for(rule:${rule.id}), content:$content")
    }
    ConditionResult(res, content, data, detail)
  }


  def compare(d1: Double, d2: Double): Boolean

  def typeFormat: String = config.`type`

  def typeTipsFormat: String = {
    if (">" == config.`type` || ">=" == config.`type`) {
      "下界"
    } else if ("<" == config.`type` || "<=" == config.`type`) {
      "上界"
    } else if ("=" == config.`type`) {
      "相同"
    } else if ("!=" == config.`type`) {
      "不同"
    } else {
      "范围"
    }
  }

  def format(v1: (Double, Double), param1: CompareIndicator): String = {
    s"${v1._2}(${v1._1} ${config.param1.`type`} ${config.param1.value})"
  }
}
