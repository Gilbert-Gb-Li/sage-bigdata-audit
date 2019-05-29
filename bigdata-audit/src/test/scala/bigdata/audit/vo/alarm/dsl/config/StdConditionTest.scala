package bigdata.audit.vo.alarm.dsl.config

import bigdata.audit.vo.audit.dsl.Condition
import bigdata.audit.vo.audit.dsl.config.std.StdCondition
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class StdConditionTest extends FunSuite with JsonMapper {
  test("xx") {
    val json =
      """
        |{
        |  "type": "std",
        |  "downBound": 2,
        |  "upBound": 2,
        |  "count": 3,
        |  "indicator": "UXIN04"
        |}
      """.stripMargin
    val config = jsonMapper.readValue(json, classOf[Condition]).asInstanceOf[StdCondition]
    assert(config.`type` === "std")
    assert(config.downBound === 2.0)
    assert(config.upBound === 2.0)
    assert(config.count === 3)
    assert(config.indicator === "UXIN04")
  }
}
