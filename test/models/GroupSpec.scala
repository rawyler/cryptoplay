package models

import org.specs2.mutable._
import play.api.db.slick.DB
import play.api.test._
import play.api.test.Helpers._
import models._
import play.api.db.slick.Config.driver.simple._
import org.joda.time.DateTime

class GroupSpec extends Specification {
  "Group model" should {
    "be queryable by team" in new WithApplication {
      DB.withSession { implicit session =>
        val team = Teams.create(Team(None, "Secure"))

        val server = Groups.create(Group(None, team.id.get, "Server"))
        val workstation = Groups.create(Group(None, team.id.get, "Workstation"))

        server.id.map { id =>
          Accounts.create(Account(None, id, "The root account", "root", "pass"))
          Accounts.create(Account(None, id, "Play", "playuser", "play"))
        }

        workstation.id.map { id =>
          Accounts.create(Account(None, id, "Alice", "alice", "a"))
          Accounts.create(Account(None, id, "Bob", "bob", "b"))
          Accounts.create(Account(None, id, "Alpher", "al", "al"))
          Accounts.create(Account(None, id, "Bethe", "be", "be"))
        }

        val serverAccounts = Accounts.findByTeamAndGroup(team.id.get, server.id.get)
        val workstationAccounts = Accounts.findByTeamAndGroup(team.id.get, workstation.id.get)

        serverAccounts.size must equalTo(2)
        workstationAccounts.size must equalTo(4)
      }
    }
  }
}