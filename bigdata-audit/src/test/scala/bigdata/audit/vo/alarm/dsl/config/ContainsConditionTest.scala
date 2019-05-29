package bigdata.audit.vo.alarm.dsl.config

import bigdata.audit.vo.audit.dsl.Condition
import bigdata.audit.vo.audit.dsl.config.ContainsCondition
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class ContainsConditionTest extends FunSuite with JsonMapper {
  test("xx") {
    val json =
      """
        |{
        |  "type": "contains",
        |  "params": [
        |    "a",
        |    "b"
        |  ]
        |}
      """.stripMargin
    val config = jsonMapper.readValue(json, classOf[Condition]).asInstanceOf[ContainsCondition]
    assert(config.`type` === "contains")
    assert(config.params == Seq("a", "b"))
  }
}
