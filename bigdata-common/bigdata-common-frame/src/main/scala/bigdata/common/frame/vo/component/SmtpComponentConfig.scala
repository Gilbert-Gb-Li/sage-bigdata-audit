package bigdata.common.frame.vo.component

import bigdata.common.frame.vo.ValidateResult

case class SmtpComponentConfig(ssl: Boolean,
                               host: String,
                               port: Int,
                               auth: Boolean,
                               user: String,
                               password: String)
  extends ComponentConfig("smtp") {
  override def valid(): ValidateResult = {
    if (host == null || host.isEmpty) {
      return ValidateResult(false, "smtp host is empty")
    }
    if (port == 0) {
      return ValidateResult(false, "smtp port is 0")
    }
    ValidateResult(true, null)
  }
}
