package bigdata.audit.vo.audit.curve

import bigdata.audit.vo.audit.AuditConfig

import scala.beans.BeanProperty

case class CurveAuditConfig(@BeanProperty bound: Double,
                            @BeanProperty field: String,
                            @BeanProperty interval: String,
                            @BeanProperty component: String,
                            @BeanProperty indices: Seq[String],
                            @BeanProperty query: Map[String, Any])
  extends AuditConfig("curve") {

}
