package bigdata.common.dsl.method

import bigdata.common.dsl.DslService

case class StaticDslService(dst: Any)
  extends DslService {

  override def process(data: Map[String, Any]): Any = {
    dst
  }
}
