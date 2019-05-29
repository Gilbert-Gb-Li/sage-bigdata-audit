package bigdata.audit.service.fetch.es

import java.util.Date

import bigdata.audit.service.fetch.FetchService
import bigdata.audit.utils.EsUtil
import bigdata.audit.vo.fetch.es.EsFetchConfig
import bigdata.common.dsl.DslService
import bigdata.common.json.FormatToString
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.index.query.{QueryBuilder, QueryBuilders}

abstract class EsFetchService(override val config: EsFetchConfig)
  extends FetchService(config) with FormatToString {

  override def process(date: Date): Double = {
    val client = EsUtil.getClient(config.component)
    if (client == null) {
      throw new Exception("can't create es conn")
    }
    handle(client, date)
  }

  def handle(client: TransportClient, date: Date): Double
}

object EsFetchService extends FormatToString {
  def createQuery(queryObject: Map[String, Any],
                  jsonMapper: ObjectMapper with ScalaObjectMapper): QueryBuilder = {
    val query = if (queryObject == null) {
      ""
    } else {
      jsonMapper.writeValueAsString(queryObject)
    }
    val qb = if (query.length > 0 && query != "{}") {
      QueryBuilders.wrapperQuery(query)
    } else {
      QueryBuilders.matchAllQuery()
    }
    qb
  }

  def createIndices(date: Date, dslServices: Seq[DslService]): Seq[String] = {
    val context = Map[String, Any]("timestamp" -> date)
    val indices = dslServices.map(dslService => {
      format(dslService.process(context))
    }).filter(_.nonEmpty)
    indices
  }


}