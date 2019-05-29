package bigdata.common.core

import java.util.Date

import org.joda.time.DateTime

trait TransToDate {
  def transToDate(any: Any): Date = {
    any match {
      case null =>
        return null
      case Some(x) =>
        x match {
          case x: Date =>
            return x

          case x: Long =>
            return new Date(x)
          case x: String =>
            val date = new DateTime(x)
            if (date != null) {
              return date.toDate
            }
          case _ =>
            return null
        }
      case x: Date =>
        return x

      case x: Long =>
        return new Date(x)
      case x: String =>
        val date = new DateTime(x)
        if (date != null) {
          return date.toDate
        }
      case _ =>
        return null
    }
    null
  }
}
