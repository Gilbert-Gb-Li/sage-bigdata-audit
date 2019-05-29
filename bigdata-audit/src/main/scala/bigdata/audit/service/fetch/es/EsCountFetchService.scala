package bigdata.audit.service.fetch.es

import java.util.Date

import bigdata.audit.vo.fetch.es.EsCountFetchConfig
import bigdata.common.dsl.{DslService, DslServiceFactory}
import bigdata.common.json.FormatToString
import org.apache.logging.log4j.{LogManager, Logger}
import org.elasticsearch.client.transport.TransportClient

case class EsCountFetchService(override val config: EsCountFetchConfig)
  extends EsFetchService(config) with FormatToString {
  private val dslServices: Seq[DslService] = config.indices.map(DslServiceFactory.parse)

  override def handle(client: TransportClient, date: Date): Double = {
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
    val res = client.prepareSearch(indices: _*).setQuery(qb).setSize(0).execute().get()
    val value = res.getHits.getTotalHits
    logger.debug(s"auditInfos:${value}")
    value.toDouble
  }
}
