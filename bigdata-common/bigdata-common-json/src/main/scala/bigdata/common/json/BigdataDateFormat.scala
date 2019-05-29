package bigdata.common.json

import java.text.{DateFormat, FieldPosition, ParsePosition, SimpleDateFormat}
import java.util.Date

class BigdataDateFormat extends DateFormat {
  val sdf1 = new SimpleDateFormat("yyyy-MM-dd")
  val sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")

  override def format(date: Date,
                      toAppendTo: StringBuffer,
                      fieldPosition: FieldPosition): StringBuffer = {
    if (date == null) {
      return null
    }
    sdf2.format(date, toAppendTo, fieldPosition)
  }

  override def parse(source: String, pos: ParsePosition): Date = {
    try {
      return sdf1.parse(source, pos)
    } catch {
      case _: Exception =>
      // pattern
    }
    sdf2.parse(source, pos)
  }
}
