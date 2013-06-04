package models

import play.api.Play.current
import play.api.db.slick.DB
import play.api.db.slick.Config.driver.simple._
import slick.lifted.{ Join, MappedTypeMapper }
import org.joda.time.DateTime
import DatabaseTypeMappers._

trait Entity[T <: Entity[T]] {
  val id: Option[Long]
  val createdAt: DateTime
  var updatedAt: DateTime
  def withId(id: Long): T
}

/**
 * A CRUD model
 */
abstract class Model[T <: Entity[T]](tableName: String) extends Table[T](None, tableName) {

  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def createdAt = column[DateTime]("createdAt", O.NotNull)
  def updatedAt = column[DateTime]("updatedAt", O.NotNull)

  protected def autoInc = * returning id

  def create(entity: T): T = DB.withSession { implicit s: Session =>
    val id = autoInc.insert(entity)
    entity.withId(id)
  }

  def findById(id: Long): Option[T] = DB.withSession { implicit s: Session =>
    findByIdQuery(id).firstOption
  }

  def findAll(): List[T] = DB.withSession { implicit s: Session =>
    findAllQuery.list
  }

  def update(entity: T): T = DB.withSession { implicit s: Session =>
    entity.id.map { id =>
      entity.updatedAt = DateTime.now()
      this.filter(_.id === id).update(entity)
    }
    entity
  }

  def delete(id: Long): Boolean = DB.withSession { implicit s: Session =>
    this.filter(_.id === id).delete > 0
  }

  lazy val findByIdQuery = for {
    id <- Parameters[Long]
    e <- this if e.id === id
  } yield e

  lazy val findAllQuery = for (entity <- this) yield entity

}