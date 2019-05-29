package bigdata.audit.use

import bigdata.audit.service.audit.FtlAlarmContent

object FreemarkerTest {
  def main(args: Array[String]): Unit = {
    import scala.collection.JavaConverters._
    val model = Map[String, Any]("ok" -> "")
    FtlAlarmContent.processStd(model.asJava)
  }
}
