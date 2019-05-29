package bigdata.common.dsl

import java.text.SimpleDateFormat

import bigdata.common.core.IoUtil
import bigdata.common.dsl.analyst.lexer.LexicalAnalysis
import org.scalatest.FunSuite

class DslServiceFactoryTest extends FunSuite {
  val sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val event = Map[String, Any](
    "app" -> "uxin",
    "table" -> "car-info",
    "timestamp" -> sdf.parse("2019-03-16 17:19:21"),
    "timestamp2" -> sdf.parse("2019-03-16 17:19:21").getTime,
    "price" -> 150000.10,
    "age" -> 3,
    "car_id" -> 123456
  )


  test("concat有嵌套") {
    val dsl =
      """
        |(concat
        |    (field "app")
        |    "_"
        |    (field "table")
        |    "_"
        |    (date-format (field "timestamp") "yyyy-MM-dd")
        |    "_"
        |    (field "car_id")
        |)
      """.stripMargin
    val service = DslServiceFactory.parse(dsl)
    val res = service.process(event)
    println(res)
    assert(res === "uxin_car-info_2019-03-16_123456")
  }
  test("concat无嵌套") {
    val dsl =
      """
        |(concat "_" 3 5 7)
      """.stripMargin
    val service = DslServiceFactory.parse(dsl)
    val res = service.process(event)
    println(res)
  }
  test("date日期对象") {
    val dsl =
      """
        |(date-format (field "timestamp") "yyyy-MM-dd")
      """.stripMargin
    val service = DslServiceFactory.parse(dsl)
    val res = service.process(event)
    println(res)
    assert(res === "2019-03-16")
  }
  test("date毫秒数") {
    val dsl =
      """
        |(date-format (field "timestamp2") "yyyy-MM-dd")
      """.stripMargin
    val service = DslServiceFactory.parse(dsl)
    val res = service.process(event)
    println(res)
    assert(res === "2019-03-16")
  }
  test("field") {
    val dsl =
      """
        |(field "app")
      """.stripMargin
    val service = DslServiceFactory.parse(dsl)
    val res = service.process(event)
    println(res)
    assert(res === "uxin")
  }

  test("static") {
    val dsl =
      """
        |(static "app")
      """.stripMargin
    val service = DslServiceFactory.parse(dsl)
    val res = service.process(event)
    println(res)
    assert(res === "app")
  }

  test("real") {
    val event = Map[String, Any](
      "app" -> "uxin",
      "table" -> "car-info",
      "timestamp" -> sdf.parse("2019-03-16 17:19:21"),
      "price" -> 150000.10,
      "age" -> 3,
      "car_id" -> 123456
    )
    val dsl =
      """
        |(concat
        |     (field 'app') '_'
        |     (field 'table') '_'
        |     (date-format (field 'timestamp')  'yyyy-MM-dd') '_'
        |     (field 'car_id')
        | )
      """.stripMargin
    val service = DslServiceFactory.parse(dsl)
    val res = service.process(event)
    println(res)
    assert(res === "uxin_car-info_2019-03-16_123456")
  }

  test("real field") {
    val event = Map[String, Any](
      "meta_table_name" -> "car-info",
      "timestamp" -> sdf.parse("2019-03-16 17:19:21"),
      "price" -> 150000.10,
      "age" -> 3,
      "car_id" -> 123456
    )
    val dsl = "(field 'car_id')"
    val service = DslServiceFactory.parse(dsl)
    val res = service.process(event)
    println(res)
    assert(res.toString === "123456")
  }
}
