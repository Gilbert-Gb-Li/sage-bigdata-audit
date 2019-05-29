package bigdata.common.dsl.method

import bigdata.common.dsl.DslService

case class NullValueDslService()
  extends DslService {

  override def process(data: Map[String, Any]): Any = {
    null
  }
}
