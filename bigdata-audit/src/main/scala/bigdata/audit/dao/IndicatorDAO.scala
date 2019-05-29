package bigdata.audit.dao

import java.sql.Timestamp

import bigdata.audit.dao.impl.{H2DbComponent, MySqlDbComponent}
import bigdata.audit.entity.Indicator
import bigdata.audit.utils.Constants

import scala.concurrent.Future

class IndicatorDAO(val dbComponent: DbComponent) {


  import dbComponent._
  import profile.api._


  class Indicators(tag: Tag) extends Table[Indicator](tag, "cep_indicator") {
    // Auto Increment the id primary key column
    def id = column[String]("ID", O.PrimaryKey)

    // The name can't be null
    def name = column[String]("NAME")

    def data = column[String]("DATA")

    def unit = column[String]("UNIT")

    def param = column[Int]("PARAM")

    def createDate = column[Timestamp]("CREATE_DATE")

    def modifyDate = column[Timestamp]("MODIFY_DATE")

    // the * projection (e.g. select * ...) auto-transforms the tupled
    // column values to / from a User
    //#mapTo
    def * = {
      (id, name, data, unit, param, createDate.?, modifyDate.?).mapTo[Indicator]
    }

    //#mapTo
  }

  val indicators = TableQuery[Indicators]


  /**
    * @param indicator
    * create new indicator
    */
  def create(indicator: Indicator): Future[Int] = db.run {
    indicators += indicator
  }

  /**
    * @param indicator
    * update existing indicator
    */
  def update(indicator: Indicator): Future[Int] = db.run {
    indicators.filter(_.id === indicator.id).update(indicator)
  }

  /**
    * @param id
    * Get indicator by id
    */
  def getById(id: String): Future[Option[Indicator]] = db.run {
    indicators.filter(_.id === id).result.headOption
  }


  /**
    * @return
    * Get all components
    */
  def getAll: Future[Seq[Indicator]] = db.run {
    indicators.result
  }

  /**
    * @param id
    * delete indicator by id
    */
  def delete(id: String): Future[Int] = db.run {
    indicators.filter(_.id === id).delete
  }
}


object IndicatorDAO {
  def apply(): IndicatorDAO = {
    Constants.dbType match {
      case Constants.dbTypeH2 =>
        new IndicatorDAO(new H2DbComponent)
      case _ =>
        new IndicatorDAO(new MySqlDbComponent)
    }
  }
}
