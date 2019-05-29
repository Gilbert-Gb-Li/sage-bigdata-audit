package bigdata.audit.service.fetch.es

import java.util
import java.util.Date

import bigdata.audit.vo.audit.curve.CurveAuditConfig
import bigdata.common.dsl.{DslService, DslServiceFactory}
import bigdata.common.json.FormatToString
import org.apache.logging.log4j.{LogManager, Logger}
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.search.aggregations.AggregationBuilders
import org.elasticsearch.search.aggregations.bucket.histogram.{DateHistogramInterval, InternalDateHistogram}
import org.joda.time.DateTimeZone

import scala.util.{Failure, Success, Try}


case class CurveFetchService(config: CurveAuditConfig) extends FormatToString {
  protected val dslServices: Seq[DslService] = config.indices.map(DslServiceFactory.parse)

  def process(client: TransportClient, date: Date): Seq[InternalDateHistogram.Bucket] = Try {
    val indices = EsFetchService.createIndices(date, dslServices)
    if (logger.isInfoEnabled()) {
      val dst = if (indices != null && indices.nonEmpty) {
        indices.mkString(",")
      } else {
        ""
      }
      logger.info(s"indices:$dst")
    }
    val qb = EsFetchService.createQuery(config.query, jsonMapper)
    val agg = AggregationBuilders.dateHistogram("curDate")
    agg.field(config.field)
    val interval = new DateHistogramInterval(config.interval)
    agg.dateHistogramInterval(interval)
    val timeZone = DateTimeZone.forID("Asia/Shanghai")
    agg.timeZone(timeZone)
    agg.minDocCount(1)

    val res = client.prepareSearch(indices: _*).setQuery(qb).
      addAggregation(agg).
      setSize(0).execute().get()
    val dateAgg = res.getAggregations.get[InternalDateHistogram]("curDate")
    val buckets: util.List[InternalDateHistogram.Bucket] = dateAgg.getBuckets
    if (buckets == null || buckets.isEmpty) {
      return Nil
    }
    (0 to buckets.size()).map(buckets.get(_))
  } match {
    case Success(x) =>
      x
    case Failure(e) =>
      logger.error("dao preData fail", e)
      Nil
  }
}
