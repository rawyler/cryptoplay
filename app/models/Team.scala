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
  var updatedAt: DateTime = DateTime.now())

object Teams extends Table[Team]("teams") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)
  def description = column[String]("description", O.Nullable)
  def noAdmin = column[Boolean]("noAdmin", O.NotNull, O.Default(false))
  def noRoot = column[Boolean]("noRoot", O.NotNull, O.Default(false))
  def createdAt = column[DateTime]("createdAt", O.NotNull)
  def updatedAt = column[DateTime]("updatedAt", O.NotNull)

  def * = id.? ~ name ~ description.? ~ noAdmin ~ noRoot ~ createdAt ~ updatedAt <> (Team, Team.unapply _)

  def create = id.? ~ name ~ description.? ~ noAdmin ~ noRoot ~ createdAt ~ updatedAt <> (Team, Team.unapply _) returning id
}