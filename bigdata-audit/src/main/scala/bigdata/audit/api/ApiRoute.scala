package bigdata.audit.api

import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

trait ApiRoute extends AlarmRuleRoute {
  def route: Route = path("hello") {
    get {
      complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Say hello to akka-http</h1>"))
    }
  } ~ pathPrefix("test") {
    path("get" / LongNumber) { id =>
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, s"${id}"))
      }
    }
  } ~ alarmRuleRoute ~ alarmRuleGetOne
}
