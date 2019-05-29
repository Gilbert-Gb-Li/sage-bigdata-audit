package bigdata.common.dsl.analyst

import bigdata.common.dsl.DslServiceFactory
import org.scalatest.FunSuite

class DslAnalysisTest extends FunSuite {
  test("xx") {
    val dsl =
      """
        |(concat
        |  'used_car_r_'
        |  (date-format
        |    (field 'timestamp')
        |    'yyyyMMdd'
        |  )
        |)
      """.stripMargin
    val res = DslServiceFactory.parse(dsl)
    println(res)
  }

  test("xx2") {
    val dsl =
      """
        |(field 'timestamp')
      """.stripMargin
    val res = DslServiceFactory.parse(dsl)
    println(res)
  }

}
