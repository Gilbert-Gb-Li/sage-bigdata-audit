package bigdata.common.dsl

trait DslService {
  def process(data: Map[String, Any]): Any

}
