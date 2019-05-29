package bigdata.common.protobuf

import bigdata.common.core.IoUtil

object ProtobufTest extends ProtobufMapper {
  override def getSchema: String = IoUtil.read("webspider.proto")


  def main(args: Array[String]): Unit = {
    println(getSchema)
  }
}
