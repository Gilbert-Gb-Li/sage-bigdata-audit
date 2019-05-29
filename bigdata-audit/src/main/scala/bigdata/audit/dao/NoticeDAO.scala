package bigdata.audit.dao

import java.sql.Timestamp

import bigdata.audit.dao.impl.{H2DbComponent, MySqlDbComponent}
import bigdata.audit.entity.Notice
import bigdata.audit.utils.Constants

import scala.concurrent.Future

object NoticeDAO {
  def apply(): NoticeDAO = {
    val dbComponent = Constants.dbType match {
      case Constants.dbTypeH2 =>
        new H2DbComponent
      case _ =>
        new MySqlDbComponent
    }
    NoticeDAO(dbComponent)
  }
}

case class NoticeDAO(dbComponent: DbComponent) {

  import dbComponent._
  import profile.api._


  class Notices(tag: Tag) extends Table[Notice](tag, "cep_notice") {
    // Auto Increment the id primary key column
    //#autoInc
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def `type` = column[String]("TYPE")

    //#autoInc
    // The name can't be null
    def name = column[String]("NAME")

    def data = column[String]("DATA")

    def createDate = column[Timestamp]("CREATE_DATE")

    def modifyDate = column[Timestamp]("MODIFY_DATE")

    // the * projection (e.g. select * ...) auto-transforms the tupled
    // column values to / from a User
    //#mapTo
    def * = (id.?, `type`, name, data, createDate.?, modifyDate.?).mapTo[Notice]

    //#mapTo
  }

  val notices = TableQuery[Notices]


  /**
    * @param notice
    * create new notice
    */
  def create(notice: Notice): Future[Int] = db.run {
    notices += notice
  }

  /**
    * @param notice
    * update existing notice
    */
  def update(notice: Notice): Future[Int] = db.run {
    notices.filter(_.id === notice.id.get).update(notice)
  }

  /**
    * @param id
    * Get notice by id
    */
  def getById(id: Long): Future[Option[Notice]] = db.run {
    notices.filter(_.id === id).result.headOption
  }

  /**
    * @return
    * Get all components
    */
  def getAll: Future[Seq[Notice]] = db.run {
    notices.result
  }

  /**
    * @param id
    * delete notice by id
    */
  def delete(id: Long): Future[Int] = db.run {
    notices.filter(_.id === id).delete
  }

}


