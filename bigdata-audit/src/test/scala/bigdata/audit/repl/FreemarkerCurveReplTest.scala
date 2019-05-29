package bigdata.audit.repl

import java.io.File

import bigdata.audit.vo.audit.curve.CurveAuditConfig
import bigdata.common.json.JsonMapper
import freemarker.template.{Configuration, DefaultObjectWrapper}
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram

import scala.collection.mutable

object FreemarkerCurveReplTest extends JsonMapper {
  def main(args: Array[String]): Unit = {
    val json1 =
      """
        |{
        |      "id": "oplogs",
        |      "name": "oplogs",
        |      "type": "curve",
        |      "bound": 0.1,
        |      "field": "timestamp",
        |      "interval": "1h",
        |      "component": "es1",
        |      "fetchType": "count",
        |      "indices": [
        |        "(concat 'used_car_r_' (date-format (field 'timestamp') 'yyyyMMdd')"
        |      ]
        |    }
      """.stripMargin
    val config = jsonMapper.readValue(json1, classOf[CurveAuditConfig])
    val cfg = new Configuration(Configuration.VERSION_2_3_28)
    cfg.setDirectoryForTemplateLoading(new File("src/main/resources/"))
    cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28))
    val root = mutable.Map[String, Any]()
    val tmp = new InternalDateHistogram.Bucket(2L, 1L, false, null, null)

//    val info = CurveAlarmInfo(ok = true, config, new Date(), Some(0.1D),
//      Seq(tmp),
//      new Date(), Some(0.1D),
//      Seq(tmp), Some(0.1))
//    root += ("vo" -> info.model)
//
//    val temp = cfg.getTemplate("conf/audit/curve.ftl")
//    val out = new ByteArrayOutputStream()
//    val writer = new OutputStreamWriter(out)
//
//    temp.process(root.asJava, writer)
//    writer.flush()
//    val json = new String(out.toByteArray())
//    println(json)
  }
}
