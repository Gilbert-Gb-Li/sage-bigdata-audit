package bigdata.audit.dao.es.agg

import java.util.Date

import bigdata.audit.service.audit.curve.CurveAuditEngineService
import bigdata.audit.vo.audit.curve.CurveAuditConfig
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class CurveAuditEngineServiceTest extends FunSuite with JsonMapper {
  test("curDate his agg") {
    val json =
      """
        |   {
        |          "match_phrase": {
        |            "k8s_container_name": {
        |              "query": "scheduler"
        |            }
        |          }
        |        }
      """.stripMargin
    val query = jsonMapper.readValue(json, classOf[Map[String, Any]])
    val config = CurveAuditConfig(1.5, "log_time", "30s", "es1",
      Seq("(concat 'oplogs_' (curDate-format (field 'timestamp') 'yyyyMMdd')"),
      query)
    val service = CurveAuditEngineService(config)
    service.process(null, new Date())

  }
}
