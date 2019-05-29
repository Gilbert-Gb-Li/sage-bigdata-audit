package bigdata.common.frame.entity

import bigdata.common.frame.vo.ValidateResult
import bigdata.common.frame.vo.component.ComponentConfig

case class Component(id: String,
                     name: String,
                     config: ComponentConfig) {
  def valid(): ValidateResult = {
    if (config == null) {
      return ValidateResult(false, "separator(config) is empty")
    }
    config.valid()
  }

  def tips(): String = {
    s"kafka, ${config.tips()}"
  }
}
