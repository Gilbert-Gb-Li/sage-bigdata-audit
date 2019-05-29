package bigdata.audit.vo.alarm.notice

case class GroupAuditRule(group: String, rules: Seq[Number]) {
  def toMap: java.util.Map[String, Any] = {
    import scala.collection.JavaConverters._

    Map[String, Any]("group" -> group,
      "rules" -> rules.map(_.longValue()).asJava
    ).asJava
  }
}
