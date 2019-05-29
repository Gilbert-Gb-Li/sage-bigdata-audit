package bigdata.audit.vo.alarm.dsl.config

import bigdata.audit.vo.audit.dsl.Condition
import bigdata.audit.vo.audit.dsl.config.logic.NotCondition
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class NotConditionTest extends FunSuite with JsonMapper {
  test("xx") {
    val json =
      """
        |{
        |  "type": "not",
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
    val config = jsonMapper.readValue(json, classOf[Condition]).asInstanceOf[NotCondition]
    assert(config.`type` === "not")
    assert(config.conditions.size === 2)
  }
}
