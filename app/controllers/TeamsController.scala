package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import models.Teams

object TeamsController extends Controller {

  def index = Action {
    Ok(views.html.teams.index())
  }

  def list(page: Int, orderBy: Int) = Action {
    Ok(views.html.teams.index())
  }

  def show(id: Long) = Action {
    Teams.findById(id).map { team =>
      Ok(views.html.teams.show(team))
    }.getOrElse(NotFound)
  }

}