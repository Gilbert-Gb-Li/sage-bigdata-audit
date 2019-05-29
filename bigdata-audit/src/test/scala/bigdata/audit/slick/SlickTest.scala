package bigdata.audit.slick


import bigdata.audit.dao.AuditRuleDAO
import bigdata.audit.entity.AuditRule

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Success

object SlickTest {
  def main(args: Array[String]): Unit = {
    val checkRuleDAO = AuditRuleDAO()

    val list = checkRuleDAO.getAll()
    Await.result(list, Duration.Inf)
    list.value match {
      case Some(Success(x: Seq[AuditRule])) =>
        x.foreach(println)
    }
  }
}
