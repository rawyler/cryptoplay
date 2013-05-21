package models

import org.joda.time.DateTime
import java.util.Locale
import DatabaseTypeMappers._
import play.api.db.slick.Config.driver.simple._
import scala.slick.driver.ExtendedProfile
import scala.slick.session.Database

object AuthenticationMode extends Enumeration {
  type AuthenticationMode = Value
  val DB = Value
}

case class User(
  id: Option[Long] = None,
  email: String,
  password: String,
  admin: Boolean,
  privateKey: String,
  publicKey: String,
  lastLoginAt: Option[DateTime],
  firstName: Option[String],
  lastName: Option[String],
  authMode: AuthenticationMode.AuthenticationMode = AuthenticationMode.DB,
  preferredLocale: Locale = Locale.forLanguageTag("en"),
  createdAt: DateTime = DateTime.now(),
  var updatedAt: DateTime = DateTime.now())

object Users extends Table[User]("users") {
  def id = column[Long]("id", O.PrimaryKey, O.AutoInc)
  def email = column[String]("email", O.NotNull)
  def password = column[String]("password", O.NotNull)
  def admin = column[Boolean]("admin", O.NotNull, O.Default(false))
  def privateKey = column[String]("privateKey", O.NotNull)
  def publicKey = column[String]("publicKey", O.NotNull)
  def lastLoginAt = column[DateTime]("lastLoginAt", O.Nullable)
  def firstName = column[String]("firstName", O.Nullable)
  def lastName = column[String]("lastName", O.Nullable)
  def authMode = column[AuthenticationMode.AuthenticationMode]("authMode", O.NotNull, O.Default(AuthenticationMode.DB))
  def preferredLocale = column[Locale]("preferredLocale", O.NotNull, O.Default(Locale.forLanguageTag("en")))
  def createdAt = column[DateTime]("createdAt", O.NotNull)
  def updatedAt = column[DateTime]("updatedAt", O.NotNull)

  def * = id.? ~ email ~ password ~ admin ~ privateKey ~ publicKey ~ lastLoginAt.? ~ firstName.? ~ lastName.? ~ authMode ~ preferredLocale ~ createdAt ~ updatedAt <> (User, User.unapply _)
}