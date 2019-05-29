package bigdata.audit.entity

import java.sql.Timestamp

import scala.beans.BeanProperty


case class Component(@BeanProperty id: String,
                     @BeanProperty name: String,
                     @BeanProperty data: String,
                     @BeanProperty createDate: Option[Timestamp],
                     @BeanProperty modifyDate: Option[Timestamp]
                    ) {

}
