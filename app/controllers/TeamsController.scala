package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Teams

object TeamsController extends Controller {

  def index = Action {
    Ok(views.html.teams.index(Teams.list(0, 10, 0)))
  }

  def show(id: Long) = Action {
    Teams.findById(id).map { team =>
      Ok(views.html.teams.show(team))
    }.getOrElse(NotFound)
  }

  def newPage() = TODO

}