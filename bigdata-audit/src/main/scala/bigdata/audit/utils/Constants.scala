package bigdata.audit.utils

import com.typesafe.config.Config

object Constants extends bigdata.common.core.Constants("audit.conf") {
  val dbType: String = getString("db.type", "h2").toLowerCase()
  val dbTypeH2: String = "h2"
  val dbTypeMySQL: String = "mysql"
  val dbConfig: Config = getConfig("db.pool")

  val apiPort: Int = getInt("api.port", 8080)
  val apiHost: String = getString("api.host", "0.0.0.0")
  val messageDeal: String = "md"

  val ftlPath = getString("ftl.path", ".")
}
