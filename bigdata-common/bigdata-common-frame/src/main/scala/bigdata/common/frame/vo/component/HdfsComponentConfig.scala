package bigdata.common.frame.vo.component

import bigdata.common.frame.vo.ValidateResult

class HdfsComponentConfig
  extends ComponentConfig("hdfs") {
  override def tips(): String = {
    "hdfs config"
  }

  override def valid(): ValidateResult = {
    ValidateResult(true, null)
  }
}
