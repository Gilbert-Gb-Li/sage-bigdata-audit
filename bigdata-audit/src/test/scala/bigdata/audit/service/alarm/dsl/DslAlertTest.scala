package bigdata.audit.service.alarm.dsl

import bigdata.audit.vo.audit.dsl.Condition
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class DslAlertTest extends FunSuite with JsonMapper {
  test("xx") {
    val json =
      """
        |{
        |  "type": "and",
        |  "conditions": [
        |    {
        |      "type": "std",
        |      "downBound": 2,
        |      "upBound": 2,
        |      "count": 3,
        |      "indicator": "UXIN04"
        |    },
        |    {
        |      "type": "gt",
        |      "param1": {
        |        "indicator": "UXIN04",
        |        "type": "*",
        |        "value": 5000
        |      },
        |      "param2": {
        |        "indicator": "UXIN04",
        |        "type": "*",
        |        "value": 5000
        |      }
        |    }
        |  ]
        |}
      """.stripMargin
    val condition = jsonMapper.readValue(json, classOf[Condition])
    val dst1 = jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(condition)
    assert(dst1 === json)
  }
}
