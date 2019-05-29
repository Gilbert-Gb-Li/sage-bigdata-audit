package bigdata.audit.vo.alarm.dsl.config

import bigdata.audit.vo.audit.dsl.compare.CompareIndicator
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class CompareIndicatorTest extends FunSuite with JsonMapper {
  test("xx") {
    val json =
      """
        |{
        |  "indicator": "UXIN04",
        |  "type": "*",
        |  "value": 5000
        |}
      """.stripMargin
    val config = jsonMapper.readValue(json, classOf[CompareIndicator])
    assert(config.indicator === "UXIN04")
    assert(config.`type` === "*")
    assert(config.value === 5000.0)
  }
}
