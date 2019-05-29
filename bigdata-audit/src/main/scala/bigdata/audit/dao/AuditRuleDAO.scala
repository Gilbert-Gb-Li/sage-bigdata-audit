package bigdata.audit.dao

import java.sql.Timestamp

import bigdata.audit.dao.impl.{H2DbComponent, MySqlDbComponent}
import bigdata.audit.entity.AuditRule
import bigdata.audit.utils.Constants

import scala.concurrent.Future

object AuditRuleDAO {
  def apply(): AuditRuleDAO = {
    Constants.dbType match {
      case Constants.dbTypeH2 =>
        new AuditRuleDAO(new H2DbComponent)
      case _ =>
        new AuditRuleDAO(new MySqlDbComponent)
    }
  }
}


class AuditRuleDAO(val dbComponent: DbComponent) {


  import dbComponent._
  import profile.api._


  class AuditRules(tag: Tag) extends Table[AuditRule](tag, "cep_audit_rule") {
    // Auto Increment the id primary key column
    //#autoInc
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    //#autoInc
    // The name can't be null
    def name = column[String]("NAME")

    def data = column[String]("DATA")

    def appId = column[String]("APP_ID")

    def createDate = column[Timestamp]("CREATE_DATE")

    def modifyDate = column[Timestamp]("MODIFY_DATE")

    // the * projection (e.g. select * ...) auto-transforms the tupled
    // column values to / from a User
    //#mapTo
    def * = (id.?, name, data, appId, createDate.?, modifyDate.?).mapTo[AuditRule]

    //#mapTo
  }

  val auditRules = TableQuery[AuditRules]


  /**
    * @param auditRule
    * create new auditRule
    */
  def create(auditRule: AuditRule): Future[Int] = db.run {
    auditRules += auditRule
  }

  /**
    * @param auditRule
    * update existing auditRule
    */
  def update(auditRule: AuditRule): Future[Int] = db.run {
    auditRules.filter(_.id === auditRule.id.get).update(auditRule)
  }

  /**
    * @param id
    * Get auditRule by id
    */
  def getById(id: Long): Future[Option[AuditRule]] = db.run {
    auditRules.filter(_.id === id).result.headOption
  }

  /**
    * @return
    * Get all components
    */
  def getAll(): Future[Seq[AuditRule]] = db.run {
    auditRules.result
  }

  /**
    * @param id
    * delete auditRule by id
    */
  def delete(id: Long): Future[Int] = db.run {
    auditRules.filter(_.id === id).delete
  }

}
