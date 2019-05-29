package bigdata.common.json

import java.text.SimpleDateFormat
import java.util.Date
import java.util.regex.Pattern

import org.joda.time.DateTime

trait FormatToString extends JsonMapper {
  private val formatSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S Z")
  private val formatPattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}(\\.\\d{3})?Z")

  def format(any: Any): String = {
    any match {
      case null =>
        ""
      case x: String =>
        if (formatPattern.matcher(x).matches()) {
          s"'${formatSdf.format(new DateTime(x).getMillis)}'"
        } else {
          x
        }
      case x: Date =>
        s"'${formatSdf.format(x)}'"
      case x: DateTime =>
        s"'${formatSdf.format(new Date(x.getMillis))}'"
      case _ =>
        jsonMapper.writeValueAsString(any)
    }
  }
}
