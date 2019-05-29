package bigdata.audit.service.fetch.es

import java.util.Date

import bigdata.audit.vo.fetch.es.EsGetFetchConfig
import bigdata.common.dsl.{DslService, DslServiceFactory}
import bigdata.common.json.FormatToString
import org.elasticsearch.client.transport.TransportClient

case class EsGetFetchService(override val config: EsGetFetchConfig)
  extends EsFetchService(config) with FormatToString {
  private val indexDslServices: DslService = DslServiceFactory.parse(config.esIndex)
  private val typeDslServices: DslService = DslServiceFactory.parse(config.esType)
  private val idDslServices: DslService = DslServiceFactory.parse(config.esId)


  override def handle(client: TransportClient, date: Date): Double = {
    val context = Map[String, Any]("timestamp" -> date)
    val esIndex = format(indexDslServices.process(context))
    val esType = format(typeDslServices.process(context))
    val esId = format(idDslServices.process(context))

    logger.info(s"index:$esIndex, type:$esType, id:$esId, column:${config.column}")
    val res = client.prepareGet(esIndex, esType, esId).execute().get()
    val map = res.getSource
    if (map == null) {
      return 0D
    }

    map.get(config.column) match {
      case null =>
        0D
      case x: Number =>
        x.doubleValue()
      case x: Any =>
        x.toString.toDouble
    }
  }
}
