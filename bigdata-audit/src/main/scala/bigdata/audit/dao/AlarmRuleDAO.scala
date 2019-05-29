package bigdata.audit.dao

import java.sql.Timestamp

import bigdata.audit.dao.impl.{H2DbComponent, MySqlDbComponent}
import bigdata.audit.entity.AlarmRule
import bigdata.audit.utils.Constants

import scala.concurrent.Future


object AlarmRuleDAO {
  def apply(): AlarmRuleDAO = {
    val dbComponent = Constants.dbType match {
      case Constants.dbTypeH2 =>
        new H2DbComponent
      case _ =>
        new MySqlDbComponent
    }
    AlarmRuleDAO(dbComponent)
  }
}

case class AlarmRuleDAO(dbComponent: DbComponent) {

  import dbComponent._
  import profile.api._


  class AlarmRules(tag: Tag) extends Table[AlarmRule](tag, "cep_alarm_rule") {
    // Auto Increment the id primary key column
    //#autoInc
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def `type` = column[String]("TYPE")

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
    def * = (id.?, `type`, name, data, appId, createDate.?, modifyDate.?).mapTo[AlarmRule]

    //#mapTo
  }

  val alarmRules = TableQuery[AlarmRules]


  /**
    * @param auditRule
    * create new auditRule
    */
  def create(auditRule: AlarmRule): Future[Int] = db.run {
    alarmRules += auditRule
  }

  /**
    * @param auditRule
    * update existing auditRule
    */
  def update(auditRule: AlarmRule): Future[Int] = db.run {
    alarmRules.filter(_.id === auditRule.id.get).update(auditRule)
  }

  /**
    * @param id
    * Get auditRule by id
    */
  def getById(id: Long): Future[Option[AlarmRule]] = db.run {
    alarmRules.filter(_.id === id).result.headOption
  }

  /**
    * @return
    * Get all components
    */
  def getAll: Future[Seq[AlarmRule]] = db.run {
    alarmRules.result
  }

  /**
    * @param id
    * delete auditRule by id
    */
  def delete(id: Long): Future[Int] = db.run {
    alarmRules.filter(_.id === id).delete
  }

}


