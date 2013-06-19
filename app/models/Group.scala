package models

import org.joda.time.DateTime
import java.util.Locale
import DatabaseTypeMappers._
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database
import play.api.Play.current
import play.api.db.slick.DB

case class Group(
  id: Option[Long] = None,
  teamId: Long,
  name: String,
  description: Option[String] = None,
  createdAt: DateTime = DateTime.now(),
  var updatedAt: DateTime = DateTime.now()) extends Entity[Group] {

  def withId(id: Long) = copy(id = Some(id))
}

case class GroupAdd(name: String, description: Option[String])

object Groups extends Model[Group]("groups") {
  def teamId = column[Long]("teamId", O.NotNull)
  def name = column[String]("name", O.NotNull)
  def description = column[String]("description", O.Nullable)

  def * = id.? ~ teamId ~ name ~ description.? ~ createdAt ~ updatedAt <> (Group, Group.unapply _)

  def team = foreignKey("fk_groups_team", teamId, Teams)(_.id)

  def findByTeam(team: Long) = DB.withSession { implicit s: Session =>
    findByTeamQuery(team).list
  }

  def findByTeamQuery(team: Long) = DB.withSession { implicit s: Session =>
    for {
      g <- Groups if g.teamId === team
    } yield g
  }
}