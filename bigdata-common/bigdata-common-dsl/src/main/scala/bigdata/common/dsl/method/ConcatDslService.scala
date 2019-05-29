package bigdata.common.dsl.method

import bigdata.common.dsl.DslService
import bigdata.common.json.FormatToString

case class ConcatDslService(params: Seq[DslService])
  extends DslService with FormatToString {
  def process(data: Map[String, Any]): Any = {
    params.map(param => {
      val s = param.process(data)
      if (s != null) {
        format(s)
      } else {
        null
      }
    }).mkString("")
  }

}
