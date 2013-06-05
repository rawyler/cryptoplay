package controllers

import play.api._
import play.api.mvc._

object Teams extends Controller {
  
  def index = Action {
    Ok
  }

  def list(page: Int, orderBy: Int) = Action {
    Ok(views.html.teams.index())
  }

}