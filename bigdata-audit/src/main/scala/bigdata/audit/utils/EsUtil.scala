package bigdata.audit.utils

import bigdata.audit.dao.ComponentDAO
import bigdata.common.frame.vo.component.EsComponentConfig
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging
import org.elasticsearch.client.transport.TransportClient

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object EsUtil extends JsonMapper with Logging {
  val cache = new mutable.WeakHashMap[String, TransportClient]

  def getClient(componentId: String): TransportClient = {
    cache.getOrElseUpdate(componentId, {
      createClient(componentId)
    })
  }

  def createClient(componentId: String): TransportClient = {

    val dao = ComponentDAO()
    val res1 = dao.getById(componentId)
    Await.result(res1, Duration.Inf) match {
      case Some(component) =>
        val esConfig = jsonMapper.readValue(component.data, classOf[EsComponentConfig])
        val client = bigdata.common.es.EsClientUtil.createClient(esConfig)
        if (client == null) {
          logger.warn("es component can't connect")
          return null
        }
        client
      case _ =>
        logger.error(s"es component($componentId) not exists")
        null
    }
  }
}
