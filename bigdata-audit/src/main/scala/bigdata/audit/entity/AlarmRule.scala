package bigdata.audit.entity

import java.sql.Timestamp

import scala.beans.BeanProperty

case class AlarmRule(@BeanProperty id: Option[Long] = None,
                     @BeanProperty `type`: String,
                     @BeanProperty name: String,
                     @BeanProperty data: String, // AuditConfig json
                     @BeanProperty appId: String,
                     @BeanProperty createDate: Option[Timestamp],
                     @BeanProperty modifyDate: Option[Timestamp]
                    ) {
  def toMap: java.util.Map[String, Any] = {
    import scala.collection.JavaConverters._

    Map[String, Any](
      "id" -> id.getOrElse("").toString,
      "type" -> `type`,
      "name" -> name,
      "auditInfos" -> data,
      "appId" -> appId,
      "createDate" -> createDate,
      "modifyDate" -> modifyDate
    ).asJava
  }

}
