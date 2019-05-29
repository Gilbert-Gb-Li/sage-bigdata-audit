package bigdata.audit.dao.impl

import bigdata.audit.dao.DbComponent
import bigdata.audit.utils.Constants


class H2DbComponent extends DbComponent {
  override val profile = slick.jdbc.H2Profile

  import profile.api._

  override val db: Database = H2DbComponent.connectionPool
}

object H2DbComponent {

  import slick.jdbc.H2Profile.api._

  val connectionPool = Database.forConfig("", Constants.dbConfig)
}