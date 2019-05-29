package bigdata.audit.dao

import slick.jdbc.JdbcProfile


trait DbComponent {
  val profile: JdbcProfile

  import profile.api._

  val db: Database
}

