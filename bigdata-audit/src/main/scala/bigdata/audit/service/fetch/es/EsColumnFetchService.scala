package bigdata.audit.service.fetch.es

import java.util.Date

import bigdata.audit.vo.fetch.es.EsColumnFetchConfig
import bigdata.common.dsl.{DslService, DslServiceFactory}
import bigdata.common.json.FormatToString
import org.apache.logging.log4j.{LogManager, Logger}
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.search.sort.SortOrder

case class EsColumnFetchService(override val config: EsColumnFetchConfig)
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
    val build = client.prepareSearch(indices: _*).setQuery(qb)
    if (config.sortField != null && config.sortField.nonEmpty &&
      config.sortType != null && config.sortType.nonEmpty) {
      val sortType = config.sortType match {
        case "desc" =>
          SortOrder.DESC
        case _ =>
          SortOrder.ASC
      }
      build.addSort(config.sortField, sortType)
    }
    build.setSize(1)
    val res = build.execute().get()

    val searchHits = res.getHits
    if (searchHits.getTotalHits == 0) {
      return 0D
    }
    val hits = res.getHits.getHits
    hits.head.getSourceAsMap.get(config.column) match {
      case x: Number =>
        x.doubleValue()
      case x: Any =>
        x.toString.toDouble
    }
  }
}
