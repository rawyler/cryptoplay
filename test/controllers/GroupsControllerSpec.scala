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
import views.html.defaultpages.badRequest
import play.api.test.FakeRequest

class GroupsControllerSpec extends Specification {

  "GroupsController" should {
    "show the create new group page for an existing team" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val noTeam = route(FakeRequest(GET, "/teams/1/groups/new")).get
        status(noTeam) must equalTo(NOT_FOUND)

        val team = Teams.create(Team(None, "team1"))

        val page = route(FakeRequest(GET, "/teams/" + team.id.get + "/groups/new")).get

        status(page) must equalTo(OK)
        contentType(page) must beSome.which(_ == "text/html")
      }
    }

    "create no group without a name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val team = Teams.create(Team(None, "team1"))

        val result = controllers.GroupsController.create(team.id.get)(FakeRequest().withFormUrlEncodedBody("description" -> "Description"))

        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "create no group without a name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val team = Teams.create(Team(None, "team1"))

        val result = controllers.GroupsController.create(team.id.get)(FakeRequest().withFormUrlEncodedBody("name" -> "Name", "description" -> "Description"))

        status(result) must equalTo(SEE_OTHER)
      }
    }

  }
}