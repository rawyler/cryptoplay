package models

import org.joda.time.DateTime
import java.util.Locale
import DatabaseTypeMappers._
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database

case class Group(
  id: Option[Long] = None,
  name: String,
  description: Option[String] = None,
  createdAt: DateTime = DateTime.now(),
  var updatedAt: DateTime = DateTime.now()) extends Entity[Group] {

  def withId(id: Long) = copy(id = Some(id))
}

object Groups extends Model[Group]("groups") {
  def name = column[String]("name", O.NotNull)
  def description = column[String]("description", O.Nullable)

  def * = id.? ~ name ~ description.? ~ createdAt ~ updatedAt <> (Group, Group.unapply _)
}