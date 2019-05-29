package bigdata.audit.vo.audit

import java.util.Date

import scala.beans.BeanProperty

case class AuditInfo(@BeanProperty id: String,
                     @BeanProperty name: String,
                     @BeanProperty content: String,
                     @BeanProperty data: String,
                     @BeanProperty alarmRuleId: Long,
                     @BeanProperty app: String,
                     @BeanProperty result: Boolean,
                     @BeanProperty detail: String,
                     @BeanProperty createDate: Date) {

  def toMap(): java.util.Map[String, Any] = {
    import scala.collection.JavaConverters._

    Map("id" -> id,
      "name" -> name,
      "content" -> content,
      "alarmRuleId" -> alarmRuleId,
      "app" -> app,
      "result" -> result,
      "createDate" -> createDate,
      "data" -> data,
      "detail" -> detail
    ).asJava
  }

}
