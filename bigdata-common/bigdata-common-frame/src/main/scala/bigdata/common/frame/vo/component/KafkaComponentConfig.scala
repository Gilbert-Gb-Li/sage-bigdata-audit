package bigdata.common.frame.vo.component

import bigdata.common.frame.vo.ValidateResult

case class KafkaComponentConfig()
  extends ComponentConfig("kafka") {
  override def tips(): String = {
    "kafka"
  }

  override def valid(): ValidateResult = {
    ValidateResult(true, null)
  }
}
