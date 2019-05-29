package bigdata.audit.api

import scala.concurrent.ExecutionContextExecutor

trait ExecutionContextRoute {
  implicit val executionContext: ExecutionContextExecutor
}
