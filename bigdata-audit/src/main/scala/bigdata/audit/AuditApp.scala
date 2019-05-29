package bigdata.audit


import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import bigdata.audit.actor.AuditActor
import bigdata.audit.actor.scheduler.SchedulerActor
import bigdata.audit.api.ApiRoute
import bigdata.audit.utils.Constants
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging

import scala.concurrent.ExecutionContextExecutor
import scala.language.postfixOps

object AuditApp extends App with ApiRoute with JsonMapper with Logging {

  // akka
  implicit val system: ActorSystem = ActorSystem("bigdata-audit")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  override implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  // rest api
  val bindingFuture = Http().bindAndHandle(route, Constants.apiHost, Constants.apiPort)

  // 审计入口
  val auditActor = system.actorOf(AuditActor.props)

  // 流程入口
  val schedulerActor = system.actorOf(SchedulerActor.props)

}
