package bigdata.audit.entity

import java.sql.Timestamp

import scala.beans.BeanProperty

case class AuditRule(@BeanProperty id: Option[Long] = None,
                     @BeanProperty name: String,
                     @BeanProperty data: String, // AuditConfig json
                     @BeanProperty appId: String,
                     @BeanProperty createDate: Option[Timestamp],
                     @BeanProperty modifyDate: Option[Timestamp]
                    ) {

}
