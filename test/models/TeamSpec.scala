package models

import org.specs2.mutable._
import play.api.db.slick.DB
import play.api.test._
import play.api.test.Helpers._
import models._
import play.api.db.slick.Config.driver.simple._
import org.joda.time.DateTime

class TeamSpec extends Specification {
  "Team model" should {
    "contain groups with accounts" in new WithApplication {
      DB.withSession { implicit session =>
        val team = Teams.create(Team(None, "Liquid", Some("Team Liquid")))

        val pros = Groups.create(Group(None, team.id.get, "Pros"))
        val fans = Groups.create(Group(None, team.id.get, "Fans"))

        pros.id.map { groupId =>
          Accounts.create(Account(None, groupId, "tlo", "tlo", "tlo"))
          Accounts.create(Account(None, groupId, "life", "life", "life"))
        }

        fans.id.map { groupId =>
          Accounts.create(Account(None, groupId, "fan1", "fan1", "fan1"))
          Accounts.create(Account(None, groupId, "fan2", "fan2", "fan2"))
          Accounts.create(Account(None, groupId, "fan3", "fan3", "fan3"))
        }
        
        val proAccounts = Accounts.filter(_.groupId === pros.id).list
        val fanAccounts = Accounts.filter(_.groupId === fans.id).list
        
        proAccounts.size === 2
        fanAccounts.size === 3
      }
    }
  }
}