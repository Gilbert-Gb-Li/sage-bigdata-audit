package bigdata.audit.service

import bigdata.audit.dao.AuditInfoDAO
import bigdata.audit.vo.audit.AuditInfo

import scala.concurrent.Future

case class AlarmInfoService() {
  val alarmInfoDAO = AuditInfoDAO()

  def save(items: Seq[AuditInfo]): Future[_] = {
    alarmInfoDAO.save(items)
  }

}
