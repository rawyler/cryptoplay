package models

import org.specs2.mutable._
import play.api.db.slick.DB
import play.api.test._
import play.api.test.Helpers._
import models._
import play.api.db.slick.Config.driver.simple._
import org.joda.time.DateTime

class UserSpec extends Specification {
  "User model" should {
    "be insertable" in new WithApplication {
      DB.withSession { implicit session =>
        val id = Users returning Users.id insert (User(None, "dude@tron.com", "dude", true, "private123", "public123", None, None, None))

        Users.where(_.id === id).firstOption must not equalTo (None)
      }
    }

    "be deletable" in new WithApplication {
      DB.withSession { implicit session =>
        val id = Users returning Users.id insert (User(None, "dude@tron.com", "dude", true, "private123", "public123", None, None, None))

        val user = for {
          u <- Users
          if u.email === "dude@tron.com"
        } yield u

        user.mutate(_.delete)

        val count = for {
          u <- Users
          if u.email === "dude@tron.com"
        } yield u.length

        (count.firstOption getOrElse (0)) must equalTo(0)
      }
    }
  }
}