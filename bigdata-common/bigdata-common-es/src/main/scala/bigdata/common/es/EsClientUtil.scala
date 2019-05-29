package bigdata.common.es

import java.net.InetAddress

import bigdata.common.frame.vo.HostPort
import bigdata.common.frame.vo.component.EsComponentConfig
import bigdata.common.log4j2.Logging
import org.elasticsearch.client.transport._
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.transport.client.PreBuiltTransportClient

import scala.util._

object EsClientUtil extends Logging {
  def createClient(component: EsComponentConfig): TransportClient =  {
    createClient(component.clusterName, component.hosts)
  }


  def createClient(clusterName: String,
                   hosts: Seq[HostPort]): TransportClient = Try {
    val settings = Settings.builder.put("cluster.name", clusterName).build
    val client = new PreBuiltTransportClient(settings)
    if (hosts == null || hosts.isEmpty) {
      logger.error("es hosts is empty")
      return null
    }
    hosts.foreach(item => {
      Try {
        client.addTransportAddress(
          new org.elasticsearch.common.transport.TransportAddress(InetAddress.getByName(item.host), item.port))
      } match {
        case Success(value) =>
          value
        case Failure(e) =>
          logger.warn(s"es hosts item(${item.host}:${item.port}) fail", e)
      }
    })
    client
  } match {
    case Success(x) => x
    case Failure(e) =>
      logger.error(s"connect to es fail:", e)
      null
  }
}
