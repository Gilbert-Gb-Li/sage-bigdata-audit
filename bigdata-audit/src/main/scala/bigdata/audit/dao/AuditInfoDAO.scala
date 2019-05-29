package bigdata.audit.dao

import bigdata.audit.AuditApp._
import bigdata.audit.vo.audit.AuditInfo
import bigdata.common.json.JsonMapper

import scala.concurrent.Future

case class AuditInfoDAO() extends JsonMapper {

  def save(items: Seq[AuditInfo]): Future[Int] = {
    // TODO
    Future {
      items.map(jsonMapper.writeValueAsString(_)).foreach(println)
      items.size
    }
  }
}
