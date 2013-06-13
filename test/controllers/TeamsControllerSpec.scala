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

    "create no team on bad request" in {
      running(FakeApplication()) {
        val result = controllers.TeamsController.create(FakeRequest())

        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "create no team without name" in {
      running(FakeApplication()) {
        val result = controllers.TeamsController.create(
          FakeRequest().withFormUrlEncodedBody("description" -> "Description", "noAdmin" -> "false", "noRoot" -> "false"))

        status(result) must equalTo(BAD_REQUEST)
      }
    }

    "create a team" in {
      running(FakeApplication()) {
        val result = controllers.TeamsController.create(
          FakeRequest().withFormUrlEncodedBody("name" -> "celtics", "description" -> "Description", "noAdmin" -> "false", "noRoot" -> "false"))

        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_ == "/teams")
      }
    }

    "destroy a team" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val noContent = controllers.TeamsController.destroy(1)(FakeRequest())

        status(noContent) must equalTo(NO_CONTENT)

        controllers.TeamsController.create(
          FakeRequest().withFormUrlEncodedBody("name" -> "celtics", "description" -> "Description", "noAdmin" -> "false", "noRoot" -> "false"))

        val result = controllers.TeamsController.destroy(1)(FakeRequest())

        status(result) must equalTo(OK)
      }
    }
  }
}