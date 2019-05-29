package bigdata.audit.entity

import java.sql.Timestamp

import scala.beans.BeanProperty


case class Indicator(@BeanProperty id: String,
                     @BeanProperty name: String,
                     @BeanProperty data: String,
                     @BeanProperty unit: String,
                     @BeanProperty param: Int,
                     @BeanProperty createDate: Option[Timestamp],
                     @BeanProperty modifyDate: Option[Timestamp]
                    ) {
  def toMap = {
    Map[String, Any]("id" -> id,
      "name" -> name,
      "auditInfos" -> data,
      "unit" -> unit,
      "param" -> param,
      "createDate" -> createDate,
      "modifyDate" -> modifyDate
    )
  }
}