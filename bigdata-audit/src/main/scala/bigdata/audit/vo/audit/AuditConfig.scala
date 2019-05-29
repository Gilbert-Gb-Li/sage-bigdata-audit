package bigdata.audit.vo.audit

import bigdata.audit.vo.audit.curve.CurveAuditConfig
import bigdata.audit.vo.audit.dsl.DslAuditConfig
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.{As, Id}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

import scala.beans.BeanProperty


@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(Array(
  new Type(value = classOf[CurveAuditConfig], name = "curve"),
  new Type(value = classOf[DslAuditConfig], name = "dsl")
))
abstract class AuditConfig(@BeanProperty val `type`: String) {

}
