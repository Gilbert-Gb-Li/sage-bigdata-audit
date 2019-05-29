package bigdata.audit.vo.alarm.dsl.config

import bigdata.audit.vo.audit.dsl.Condition
import bigdata.audit.vo.audit.dsl.compare.GtCondition
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class GtConditionTest extends FunSuite with JsonMapper {
  test("xx") {
    val json =
      """
        |{
        |  "type": "gt",
        |  "param1": {
        |    "indicator": "UXIN04",
        |    "type": "*",
        |    "value": 5000
        |  },
        |  "param2": {
        |    "indicator": "UXIN04",
        |    "type": "*",
        |    "value": 5000
        |  }
        |}
      """.stripMargin
    val config = jsonMapper.readValue(json, classOf[Condition]).asInstanceOf[GtCondition]
    assert(config.`type` === "gt")
    assert(config.param1.indicator === "UXIN04")
    assert(config.param1.`type` === "*")
    assert(config.param1.value === 5000.0)
    assert(config.param2.indicator === "UXIN04")
    assert(config.param2.`type` === "*")
    assert(config.param2.value === 5000.0)
  }
}
