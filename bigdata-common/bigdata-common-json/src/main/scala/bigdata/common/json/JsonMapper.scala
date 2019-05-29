package bigdata.common.json

import bigdata.common.log4j2.Logging
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

trait JsonMapper extends Logging {

  final val jsonMapper: ObjectMapper with ScalaObjectMapper = newMapper()

  def newMapper(): ObjectMapper with ScalaObjectMapper = {
    val mapper = new ObjectMapper() with ScalaObjectMapper

    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY)
    mapper.registerModule(DefaultScalaModule)
    mapper.setSerializationInclusion(Include.NON_ABSENT)
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    mapper.configure(SerializationFeature.INDENT_OUTPUT, false)
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    //    mapper.setDateFormat(new BigdataDateFormat())
    mapper
  }

  def toJson(x: Any, pretty: Option[String] = None): String = {
    pretty match {
      case Some(x: String) if x != "false" =>
        jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(x)
      case _ =>
        jsonMapper.writeValueAsString(x)
    }
  }
}
