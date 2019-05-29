package bigdata.audit.vo.alarm.dsl.config

import bigdata.audit.vo.audit.dsl.Condition
import bigdata.audit.vo.audit.dsl.config.logic.OrCondition
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class OrConditionTest extends FunSuite with JsonMapper {
  test("xx") {
    val json =
      """
        |{
        |  "type": "or",
        |  "conditions": [
        |    {
        |      "type": "lt",
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
        |    },
        |    {
        |      "type": "lt",
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
    val config = jsonMapper.readValue(json, classOf[Condition]).asInstanceOf[OrCondition]
    assert(config.`type` === "or")
    assert(config.conditions.size === 2)
  }
}
