package bigdata.audit.vo.alarm

case class AlarmScheduler(cron: String,
                          offset: Int,
                          unit: String) {
  def toMap: java.util.Map[String, Any] = {
    import scala.collection.JavaConverters._

    Map("cron" -> cron,
      "offset" -> offset,
      "unit" -> unit
    ).asJava
  }
}
