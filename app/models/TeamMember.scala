package models

import org.joda.time.DateTime
import java.util.Locale
import DatabaseTypeMappers._
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database

case class TeamMember(
  id: Option[Long],
  teamId: Long,
  userId: Long,
  password: String,
  admin: Boolean = false,
  locked: Boolean = false,
  createdAt: DateTime = DateTime.now(),
  var updatedAt: DateTime = DateTime.now()) extends Entity[TeamMember] {

  def withId(id: Long) = copy(id = Some(id))
}

object TeamMembers extends Model[TeamMember]("teammembers") {
  def teamId = column[Long]("teamId", O.NotNull)
  def userId = column[Long]("userId", O.NotNull)
  def password = column[String]("password", O.NotNull)
  def admin = column[Boolean]("admin", O.Default(false))
  def locked = column[Boolean]("locked", O.Default(false))

  def * = id.? ~ teamId ~ userId ~ password ~ admin ~ locked ~ createdAt ~ updatedAt <> (TeamMember, TeamMember.unapply _)

  def team = foreignKey("team_fk", teamId, Teams)(_.id)
  def user = foreignKey("user_fk", userId, Users)(_.id)

  def idx = index("idx_team_user", (teamId, userId), unique = true)
}