package bigdata.audit.vo.audit.dsl

import bigdata.audit.vo.audit.AuditConfig

import scala.beans.BeanProperty

case class DslAuditConfig(@BeanProperty condition: Condition, indicator: String)
  extends AuditConfig("dsl") {

}
