package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Team
import models.Teams

class TeamsControllerSpec extends Specification {

  "Teams" should {
    "render the index page" in {
      running(FakeApplication()) {
        val page = route(FakeRequest(GET, "/teams")).get

        status(page) must equalTo(OK)
        contentType(page) must beSome.which(_ == "text/html")
      }
    }

    "render the team show page" in {
      running(FakeApplication()) {
        val team = Teams.create(Team(None, ""))

        val page = route(FakeRequest(GET, "/teams/" + team.id.get)).get

        status(page) must equalTo(OK)
        contentType(page) must beSome.which(_ == "text/html")
      }
    }
  }
}