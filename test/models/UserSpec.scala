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

        Users.delete(id)

        val count = for {
          u <- Users
          if u.email === "dude@tron.com"
        } yield u.length

        (count.firstOption getOrElse (0)) must equalTo(0)
      }
    }

    "be part of a team" in new WithApplication {
      DB.withSession { implicit session =>
        val team1 = Teams.create(Team(None, "Rul0rz"))
        val team2 = Teams.create(Team(None, "Bash0rz"))
        val team3 = Teams.create(Team(None, "Kick0rz"))
        val user = Users.create(User(None, "boba@fett.com", "kjasu72hal98", false, "pr1", "pu1", None, None, None))

        TeamMembers.insert(TeamMember(None, team1.id.get, user.id.get, "pass"))
        TeamMembers.insert(TeamMember(None, team2.id.get, user.id.get, "pass"))
        TeamMembers.insert(TeamMember(None, team3.id.get, user.id.get, "pass"))

        val teams = for {
          tm <- TeamMembers
          t <- tm.team if tm.userId === user.id
        } yield t

        teams.list must containAllOf(List(team1, team2, team3))
      }
    }
  }
}