package bigdata.audit.vo.audit.dsl.compare

/**
  *
  * @param indicator 指标ID
  * @param `type`    +、-、*、/
  * @param value     参数值
  */
case class CompareIndicator(indicator: String,
                            `type`: String,
                            value: Double) {

}
