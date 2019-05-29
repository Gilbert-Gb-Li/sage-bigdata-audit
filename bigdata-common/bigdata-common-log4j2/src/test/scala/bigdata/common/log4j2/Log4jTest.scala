package bigdata.common.log4j2

import org.scalatest.FunSuite

class Log4jTest extends FunSuite with Logging {
  test("xx") {
    logger.error("xx")
  }
}
