package bigdata.common.dsl.method

import bigdata.common.dsl.DslService
import bigdata.common.json.FormatToString

case class FieldDslService(fieldService: DslService)
  extends DslService with FormatToString {
  def process(data: Map[String, Any]): Any = {
    if (data == null) {
      return null
    }
    val field = format(fieldService.process(data))
    if (field == null || field.isEmpty) {
      return null
    }
    data.get(field) match {
      case Some(x) =>
        x
      case None =>
        null
    }
  }
}
