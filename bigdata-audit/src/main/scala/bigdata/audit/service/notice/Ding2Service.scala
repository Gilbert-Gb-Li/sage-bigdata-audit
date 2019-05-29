package bigdata.audit.service.notice

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import bigdata.audit.AuditApp
import bigdata.audit.dao.ComponentDAO
import bigdata.audit.vo.notice.Ding2Config
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging

import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class Ding2Service(config: Ding2Config)
  extends NoticeService with JsonMapper with Logging {
  val dao = ComponentDAO()

  def process(title: String, content: String): Boolean = {
    val jsonObj = Ding2Body("markdown",
      Markdown(title, content),
      At(config.atMobiles, config.isAtAll)
    )
    val body = jsonMapper.writeValueAsString(jsonObj)
    val system = if (AuditApp.system != null) {
      AuditApp.system
    } else { // only for test unit
      ActorSystem("bigdata-audit")
    }
    val future = Http()(system).singleRequest(HttpRequest(uri = config.url,
      method = HttpMethods.POST,
      entity = HttpEntity(ContentTypes.`application/json`, body))
    )
    Await.result(future, Duration.Inf)
    if (logger.isDebugEnabled) {
      logger.debug(s"alarm content:$body")
    }
    logger.info("send ding2 ok")
    true
  }

  case class At(atMobiles: Seq[String] = Nil, isAtAll: Boolean = false)

  case class Markdown(title: String, text: String)

  case class Ding2Body(msgtype: String,
                       markdown: Markdown,
                       at: At
                      )

}
