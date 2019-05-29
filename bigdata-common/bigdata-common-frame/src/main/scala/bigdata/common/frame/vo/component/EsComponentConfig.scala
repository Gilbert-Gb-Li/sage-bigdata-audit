package bigdata.common.frame.vo.component

import bigdata.common.frame.vo.{HostPort, ValidateResult}

case class EsComponentConfig(clusterName: String,
                             hosts: Seq[HostPort])
  extends ComponentConfig("es") {
  override def valid(): ValidateResult = {
    if (clusterName == null) {
      return ValidateResult(false, "es clusterName is empty")
    }
    if (hosts == null || hosts.isEmpty) {
      return ValidateResult(false, "es hosts is empty")
    }
    ValidateResult(true, null)
  }
}
