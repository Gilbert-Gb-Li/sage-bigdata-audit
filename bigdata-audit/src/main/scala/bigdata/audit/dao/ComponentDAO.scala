package bigdata.audit.dao

import java.sql.Timestamp

import bigdata.audit.dao.impl.{H2DbComponent, MySqlDbComponent}
import bigdata.audit.entity.Component
import bigdata.audit.utils.Constants

import scala.concurrent.Future


object ComponentDAO {
  def apply(): ComponentDAO = {
    Constants.dbType match {
      case Constants.dbTypeH2 =>
        new ComponentDAO(new H2DbComponent)
      case _ =>
        new ComponentDAO(new MySqlDbComponent)
    }
  }
}

class ComponentDAO(val dbComponent: DbComponent) {


  import dbComponent._
  import profile.api._


  class Components(tag: Tag) extends Table[Component](tag, "cep_component") {
    // Auto Increment the id primary key column
    //#autoInc
    def id = column[String]("ID", O.PrimaryKey)

    //#autoInc
    // The name can't be null
    def name = column[String]("NAME")

    def data = column[String]("DATA")

    def createDate = column[Timestamp]("CREATE_DATE")

    def modifyDate = column[Timestamp]("MODIFY_DATE")

    // the * projection (e.g. select * ...) auto-transforms the tupled
    // column values to / from a User
    //#mapTo
    def * = (id, name, data, createDate.?, modifyDate.?).mapTo[Component]

    //#mapTo
  }

  val components = TableQuery[Components]


  /**
    * @param component
    * create new component
    */
  def create(component: Component): Future[Int] = db.run {
    components += component
  }

  /**
    * @param component
    * update existing component
    */
  def update(component: Component): Future[Int] = db.run {
    components.filter(_.id === component.id).update(component)
  }

  /**
    * @param id
    * Get component by id
    */
  def getById(id: String): Future[Option[Component]] = db.run {
    components.filter(_.id === id).result.headOption
  }

  /**
    * @return
    * Get all components
    */
  def getAll(): Future[Seq[Component]] = db.run {
    components.result
  }

  /**
    * @param id
    * delete component by id
    */
  def delete(id: String): Future[Int] = db.run {
    components.filter(_.id === id).delete
  }

}


