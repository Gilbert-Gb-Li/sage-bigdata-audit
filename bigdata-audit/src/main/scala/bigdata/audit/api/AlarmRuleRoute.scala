package bigdata.audit.api

import java.text.SimpleDateFormat
import java.util.Date

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import bigdata.audit.AuditApp
import bigdata.audit.dao.AuditRuleDAO
import bigdata.audit.entity.AuditRule
import bigdata.audit.utils.Constants
import bigdata.common.json.JsonMapper

import scala.concurrent.Future

trait AlarmRuleRoute extends JsonMapper with ExecutionContextRoute {
  def alarmRuleRoute: Route = pathPrefix("alarm") {
    alarmRuleGetOne ~ alarmRuleExecute ~ alarmRuleList
  }

  def alarmRuleList: Route = path("rule") {
    parameter('page.?, 'size.?, 'pretty.?) {
      (page, size, pretty) =>
        complete(s"""{"message":"xx"}""")
    }
  }

  def alarmRuleExecute: Route = post {
    path("execute" / LongNumber) { id =>
      entity(as[String]) {
        json =>
          try {
            val date: Date = json match {
              case null | "" =>
                new Date()
              case _ =>
                val data = jsonMapper.readValue(json, classOf[Map[String, Any]])
                data.get("date") match {
                  case Some(x: String) =>
                    val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    sdf.parse(x)
                  case _ =>
                    new Date()
                }
            }

            AuditApp.auditActor ! (Constants.messageDeal, id, date)
            complete(s"""{"message":"已经添加到执行队列"}""")
          } catch {
            case e: Exception =>
              complete(s"""{"message":"参数错误"}""")
          }
      }
    }
  }

  def alarmRuleGetOne: Route = path("rule" / LongNumber) { id =>
    get {
      parameter('pretty.?) {
        pretty =>
          val ruleDAO = AuditRuleDAO()
          val maybeItem: Future[Option[AuditRule]] = ruleDAO.getById(id)

          onSuccess(maybeItem) {
            case Some(item) => complete(toJson(item, pretty))
            case None => complete(StatusCodes.NotFound)
          }
      }

    }
  }
}
