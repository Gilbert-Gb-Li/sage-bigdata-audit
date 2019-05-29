package bigdata.audit.vo.audit.dsl.config.std

import bigdata.audit.vo.audit.dsl.Condition

case class StdCondition(indicator: String, count: Int,
                        downBound: Double, upBound: Double)
  extends Condition("std") {
  override def getIndicators(): Seq[String] = {
    Seq(indicator)
  }
}
