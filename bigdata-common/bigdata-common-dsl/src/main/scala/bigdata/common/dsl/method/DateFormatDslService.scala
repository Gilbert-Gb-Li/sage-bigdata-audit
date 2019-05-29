package bigdata.common.dsl.method

import java.text.SimpleDateFormat
import java.util
import java.util.Date

import bigdata.common.core.TransToDate
import bigdata.common.dsl.DslService
import bigdata.common.json.FormatToString
import org.apache.logging.log4j.{LogManager, Logger}

import scala.util.{Failure, Success, Try}

case class DateFormatDslService(fieldDslService: DslService, patternDslService: DslService)
  extends DslService with FormatToString with TransToDate {

  private val cache = new util.WeakHashMap[String, SimpleDateFormat]()


  def handle(data: Map[String, Any], date: Date): Any = {
    val pattern = format(patternDslService.process(data))

    var sdf = cache.get(pattern)
    if (sdf != null) {
      return sdf.format(date)
    }
    sdf = new SimpleDateFormat(pattern)
    cache.put(pattern, sdf)
    sdf.format(date)
  }

  def process(data: Map[String, Any]): Any = Try {
    if (data == null) {
      return null
    }
    val field = fieldDslService.process(data)
    val date = transToDate(field)
    if (date == null) {
      return null
    }
    handle(data, date)
  } match {
    case Success(x) =>
      x
    case Failure(e) =>
      logger.error(s"format date fail", e)
      null
  }
}
