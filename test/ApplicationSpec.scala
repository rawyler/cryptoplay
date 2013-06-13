package test

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in {
      running(FakeApplication()) {
        route(FakeRequest(GET, "/boum")) must beNone
      }
    }

    "redirect to the teams index page on /" in {
      running(FakeApplication()) {
        val result = controllers.Application.index(FakeRequest())

        status(result) must equalTo(SEE_OTHER)
        redirectLocation(result) must beSome.which(_ == "/teams")
      }
    }
  }
}