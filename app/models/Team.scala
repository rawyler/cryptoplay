package models

import org.joda.time.DateTime
import java.util.Locale
import DatabaseTypeMappers._
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database

case class Team(
  id: Option[Long] = None,
  name: String,
  description: Option[String] = None,
  noAdmin: Boolean = false,
  noRoot: Boolean = false,
  createdAt: DateTime = DateTime.now(),
  var updatedAt: DateTime = DateTime.now()) extends Entity[Team] {

  def withId(id: Long) = copy(id = Some(id))
}

object Teams extends Model[Team]("teams") {
  def name = column[String]("name", O.NotNull)
  def description = column[String]("description", O.Nullable)
  def noAdmin = column[Boolean]("noAdmin", O.NotNull, O.Default(false))
  def noRoot = column[Boolean]("noRoot", O.NotNull, O.Default(false))

  def * = id.? ~ name ~ description.? ~ noAdmin ~ noRoot ~ createdAt ~ updatedAt <> (Team, Team.unapply _)
}