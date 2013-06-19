package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data._
import play.api.data.Forms._
import models.Teams
import models.Team
import views.html.defaultpages.badRequest
import models.Groups

object TeamsController extends Controller {

  val Home = Redirect(routes.TeamsController.index)

  def index = Action { implicit request =>
    Ok(views.html.teams.index(Teams.list(0, 10, 0)))
  }

  def newPage = Action { implicit request =>
    Ok(views.html.teams.newPage(teamForm))
  }

  def create = Action { implicit request =>
    teamForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.teams.newPage(formWithErrors)),
      team => {
        Teams.create(team)
        Home.flashing("success" -> Messages("teams.created", team.name))
      })
  }

  def show(id: Long) = Action { implicit request =>
    Teams.findById(id).map { team =>
      Ok(views.html.teams.show(team, Groups.findByTeam(id)))
    }.getOrElse(NotFound)
  }

  def edit(id: Long) = Action { implicit request =>
    Teams.findById(id).map { team =>
      Ok(views.html.teams.edit(id, teamForm.fill(team)))
    }.getOrElse(NotFound)
  }

  def update(id: Long) = Action { implicit request =>
    teamForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.teams.edit(id, formWithErrors)),
      team => {
        Teams.update(team.withId(id))
        Home.flashing("success" -> Messages("teams.updated", team.name))
      })
  }

  def destroy(id: Long) = Action { implicit request =>
    if (Teams.delete(id)) Home.flashing("success" -> Messages("teams.deleted")) else NoContent
  }

  def teamForm = Form(
    mapping(
      "id" -> optional(longNumber),
      "name" -> nonEmptyText,
      "description" -> optional(text),
      "noAdmin" -> boolean,
      "noRoot" -> boolean)((id, name, description, noAdmin, noRoot) => Team(id, name, description, noAdmin, noRoot))((team: Team) => Some(team.id, team.name, team.description, team.noAdmin, team.noRoot)))
}