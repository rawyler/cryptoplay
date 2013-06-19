package models

import org.joda.time.DateTime
import java.util.Locale
import DatabaseTypeMappers._
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database
import play.api.Play.current
import play.api.db.slick.DB

case class Account(
  id: Option[Long] = None,
  groupId: Long,
  name: String,
  username: String,
  password: String,
  description: Option[String] = None,
  createdAt: DateTime = DateTime.now(),
  var updatedAt: DateTime = DateTime.now()) extends Entity[Account] {

  def withId(id: Long) = copy(id = Some(id))
}

case class AccountAdd(name: String, username: String, password: String, description: Option[String])

object Accounts extends Model[Account]("accounts") {
  def groupId = column[Long]("groupId", O.NotNull)
  def name = column[String]("name", O.NotNull)
  def username = column[String]("username", O.NotNull)
  def password = column[String]("password", O.NotNull)
  def description = column[String]("description", O.Nullable)

  def * = id.? ~ groupId ~ name ~ username ~ password ~ description.? ~ createdAt ~ updatedAt <> (Account, Account.unapply _)

  def group = foreignKey("fk_accounts_group", groupId, Groups)(_.id)

  def findByTeamAndGroup(team: Long, group: Long) = DB.withSession { implicit s: Session =>
    findByTeamAndGroupQuery(team, group).list
  }

  def findByTeamAndGroupQuery(team: Long, group: Long) = for {
    a <- Accounts if a.groupId === group
    g <- a.group if g.teamId === team
  } yield a
}