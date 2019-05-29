package bigdata.audit.repl

import java.io.File

import freemarker.template.{Configuration, DefaultObjectWrapper}

import scala.collection.mutable

object FreemarkerCountReplTest {
  def main(args: Array[String]): Unit = {
    val cfg = new Configuration(Configuration.VERSION_2_3_28)
    cfg.setDirectoryForTemplateLoading(new File("src/main/resources/"))
    cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28))
    val root = mutable.Map[String, Any]()
//    val config = StandardDeviationAlarmConfig(0.1D, 0.1D, 3, null)
//    val info = StandardDeviationAlarmInfo(ok = true, config, Seq[Double](0.2D, 0.3D, 0.4D),
//      new Date(), 117D, 0.1D, 0.2D, 0.3D)
//    root += ("vo" -> info.model)
//
//    val temp = cfg.getTemplate("conf/audit/std.ftl")
//    val out = new ByteArrayOutputStream()
//    val writer = new OutputStreamWriter(out)
//
//    temp.process(root.asJava, writer)
//    writer.flush()
//    val json = new String(out.toByteArray())
//    println(json)
  }
}
