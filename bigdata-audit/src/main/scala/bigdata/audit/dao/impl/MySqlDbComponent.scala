package bigdata.audit.dao.impl

import bigdata.audit.dao.DbComponent
import bigdata.audit.utils.Constants


class MySqlDbComponent extends DbComponent {
  override val profile = slick.jdbc.MySQLProfile

  import profile.api._

  override val db: Database = MySqlDbComponent.connectionPool
}

object MySqlDbComponent {

  import slick.jdbc.MySQLProfile.api._

  val connectionPool = Database.forConfig("", Constants.dbConfig)
}
