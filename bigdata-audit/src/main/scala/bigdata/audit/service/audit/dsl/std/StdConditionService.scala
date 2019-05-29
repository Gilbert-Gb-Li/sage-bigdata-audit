package bigdata.audit.service.audit.dsl.std

import java.text.NumberFormat
import java.util.{Calendar, Date}

import bigdata.audit.entity.{AuditRule, Indicator}
import bigdata.audit.service.audit.FtlAlarmContent
import bigdata.audit.service.audit.dsl.{ConditionResult, ConditionService}
import bigdata.audit.service.fetch.FetchService
import bigdata.audit.utils.VarianceUtils
import bigdata.audit.vo.audit.dsl.DslAuditConfig
import bigdata.audit.vo.audit.dsl.config.std.StdCondition
import bigdata.audit.vo.fetch.FetchConfig
import bigdata.common.json.JsonMapper

case class StdConditionService(config: StdCondition)
  extends ConditionService(config) with JsonMapper {
  private val nf: NumberFormat = NumberFormat.getInstance()

  def process(rule: AuditRule, dslAlarmConfig: DslAuditConfig, curDate: Date,
              indicators: Map[String, Indicator]): ConditionResult = {
    val indicator: Indicator = indicators.get(config.indicator) match {
      case Some(x) =>
        x
      case _ =>
        return null
    }
    val fetchConfig = jsonMapper.readValue(indicator.data, classOf[FetchConfig])
    val fetchService = FetchService(fetchConfig)

    val days = config.count
    val calendar = Calendar.getInstance()
    val data1 = (0 to days).map(i => {
      calendar.setTime(curDate)
      indicator.unit match {
        case "week" | "w" =>
          calendar.set(Calendar.DAY_OF_WEEK, indicator.param)
          calendar.add(Calendar.WEEK_OF_YEAR, -i)
        case "month" | "m" =>
          calendar.set(Calendar.DAY_OF_MONTH, indicator.param)
          calendar.add(Calendar.MONTH, -i)
        case "year" | "y" =>
          calendar.set(Calendar.DAY_OF_YEAR, indicator.param)
          calendar.add(Calendar.YEAR, -i)
        case "day" | "d" | _ =>
          calendar.add(Calendar.DAY_OF_MONTH, -i)
      }


      val dstDate = calendar.getTime
      try {
        fetchService.process(dstDate)
      } catch {
        case e: Exception =>
          if (e.getMessage.contains("IndexNotFoundException")) {
            0
          } else {
            logger.error("fetch auditInfos fail", e)
            return null
          }
      }
    })
    val curData = data1.head
    val preData = data1.tail.reverse

    val stats = VarianceUtils.std(preData)

    val up: Double = stats.std * config.upBound
    val down: Double = -stats.std * config.downBound
    val bound: Double = Math.abs(curData - stats.avg)
    val res = bound >= down && bound <= up


    val content: String = try {
      import scala.collection.JavaConverters._

      val model = Map[String, Any]("ok" -> res,
        "curDate" -> curDate,
        "curData" -> curData,
        "preData" -> preData.asJava,
        "avg" -> stats.avg,
        "std" -> stats.std,
        "variance" -> stats.variance,
        "config" -> config,
        "rule" -> rule,
        "bound" -> bound,
        "indicator" -> indicator.toMap
      )
      FtlAlarmContent.processStd(model.asJava)
    } catch {
      case e: Exception =>
        logger.error("gen std content fail", e)
        ""
    }
    val data = if (dslAlarmConfig.indicator == config.indicator) {
      nf.format(new java.math.BigDecimal(String.valueOf(curData)))
    } else {
      null
    }
    val detail = s"$res 标准差"
    if (logger.isDebugEnabled()) {
      logger.debug(s"condition(${toJson(config)}) for(rule:${rule.id}), content:$content")
    }
    ConditionResult(res, content, data, detail)
  }
}
