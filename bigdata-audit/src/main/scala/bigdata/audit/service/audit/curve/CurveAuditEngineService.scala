package bigdata.audit.service.audit.curve

import java.util.{Calendar, Date}

import bigdata.audit.entity.AuditRule
import bigdata.audit.service.audit.AuditEngineService
import bigdata.audit.service.fetch.es.CurveFetchService
import bigdata.audit.utils.{EsUtil, VarianceUtils}
import bigdata.audit.vo.audit.AuditInfo
import bigdata.audit.vo.audit.curve.CurveAuditConfig

case class CurveAuditEngineService(config: CurveAuditConfig)
  extends AuditEngineService {
  val fetchService = CurveFetchService(config)

  def valid: Boolean = {
    if (config == null) {
      return false
    }
    if (fetchService == null) {
      return false
    }
    true
  }

  override def process(rule: AuditRule, curDate: Date): AuditInfo = {
    val client = EsUtil.getClient(config.component)

    val calendar = Calendar.getInstance()
    calendar.setTime(curDate)
    calendar.add(Calendar.DAY_OF_MONTH, -1)

    val preDate = calendar.getTime


    val curData = fetchService.process(client, curDate)

    if (curData.isEmpty) {
      return null //CurveAlarmInfo(ok = false, config, curDate)
    }
    val curVariance = VarianceUtils.variance(curData.map(item => {
      item.getDocCount.toDouble
    }))

    val preData = fetchService.process(client, preDate)
    if (preData.isEmpty) {
      // TODO
      return null; //CurveAlarmInfo(ok = false, config, curDate)
    }
    val preVariance = VarianceUtils.variance(preData.map(item => {
      item.getDocCount.toDouble
    }))

    val bound = Math.abs(curVariance.variance - preVariance.variance) / preVariance.variance
    val res = bound <= config.bound
    //    val info = CurveAlarmInfo(res, config, curDate, Some(curVariance.variance), curData, preDate, Some(preVariance.variance), preData, Some(bound))
    val model: java.util.Map[String, Any] = {
      import scala.collection.JavaConverters._
      Map[String, Any]("ok" -> res,
        "curDate" -> curDate,
        "curVariance" -> curVariance.variance,
        "curData" -> curData.asJava,
        "preDate" -> preDate,
        "preVariance" -> preVariance.variance,
        "preData" -> preData.asJava,
        "bound" -> bound
      ).asJava
    }
    // TODO
    null
  }
}
