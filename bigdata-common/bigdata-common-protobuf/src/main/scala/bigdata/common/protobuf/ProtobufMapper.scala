package bigdata.common.protobuf

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.PropertyAccessor
import com.fasterxml.jackson.databind.{ObjectMapper, SerializationFeature}
import com.fasterxml.jackson.dataformat.protobuf.ProtobufFactory
import com.fasterxml.jackson.dataformat.protobuf.schema.ProtobufSchemaLoader
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

trait ProtobufMapper {
  def getSchema: String

  val pbMapper: ObjectMapper with ScalaObjectMapper = newPbMapper(getSchema)

  def newPbMapper(schema: String): ObjectMapper with ScalaObjectMapper = {
    val protobufSchema = ProtobufSchemaLoader.std.parse(schema)
    val mapper = new ObjectMapper(new ProtobufFactory) with ScalaObjectMapper
    mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY)
    // jsonMapper.registerModule(DefaultScalaModule).setSerializationInclusion(Include.NON_NULL)
    mapper.registerModule(DefaultScalaModule).setSerializationInclusion(Include.NON_ABSENT)
    mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    mapper.readerFor(classOf[Map[String, Any]]).`with`(protobufSchema)
    mapper
  }
}
