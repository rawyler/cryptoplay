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

    "be part of a team" in new WithApplication {
      DB.withSession { implicit session =>
        val teamId1 = Teams.create.insert(Team(None, "Rul0rz"))
        val teamId2 = Teams.create.insert(Team(None, "Bash0rz"))
        val teamId3 = Teams.create.insert(Team(None, "Kick0rz"))
        val userId = Users.create.insert(User(None, "boba@fett.com", "kjasu72hal98", false, "pr1", "pu1", None, None, None))

        TeamMembers.insert(TeamMember(teamId1, userId, "pass"))
        TeamMembers.insert(TeamMember(teamId2, userId, "pass"))
        TeamMembers.insert(TeamMember(teamId3, userId, "pass"))

        val teams = for {
          tm <- TeamMembers
          t <- tm.team if tm.userId === userId
        } yield t

        val result = teams.list.map(_.id.get) must containAllOf(List(teamId1, teamId2, teamId3))
      }
    }
  }
}