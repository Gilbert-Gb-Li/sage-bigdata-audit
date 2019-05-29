package bigdata.common.frame.vo.component

import bigdata.common.frame.vo.ValidateResult
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.{As, Id}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(Array(
  new Type(value = classOf[KafkaComponentConfig], name = "kafka"),
  new Type(value = classOf[HdfsComponentConfig], name = "hdfs"),
  new Type(value = classOf[SmtpComponentConfig], name = "smtp"),
  new Type(value = classOf[EsComponentConfig], name = "es")
))
abstract class ComponentConfig(`type`: String) {
  def tips(): String = {
    s"component(${`type`})"
  }

  def valid(): ValidateResult
}
