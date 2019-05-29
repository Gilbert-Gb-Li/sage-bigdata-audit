package bigdata.audit.vo.audit.dsl.config

import bigdata.audit.vo.audit.dsl.Condition

case class ContainsCondition(indicator: String, params: Seq[String])
  extends Condition("contains") {
  override def getIndicators(): Seq[String] = {
    Seq(indicator)
  }
}
