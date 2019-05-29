package bigdata.audit.entity

import java.sql.Timestamp

import scala.beans.BeanProperty

case class Notice(@BeanProperty id: Option[Long] = None,
                  @BeanProperty `type`: String,
                  @BeanProperty name: String,
                  @BeanProperty data: String, // AuditConfig json
                  @BeanProperty createDate: Option[Timestamp],
                  @BeanProperty modifyDate: Option[Timestamp]
                 ) {

}
