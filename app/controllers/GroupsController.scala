package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data._
import play.api.data.Forms._
import models.Groups
import models.Group
import models.GroupAdd
import views.html.defaultpages.badRequest
import models.Teams
import models.Accounts

object GroupsController extends Controller {

  val Home = Redirect(routes.TeamsController.index)

  def newPage(team: Long) = Action { implicit request =>
    Teams.findById(team).map { _ =>
      Ok(views.html.groups.newPage(team, groupForm))
    }.getOrElse(NotFound)
  }

  def create(team: Long) = Action { implicit request =>
    groupForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.groups.newPage(team, formWithErrors)),
      group => {
        Groups.create(Group(None, team, group.name, group.description))
        Home.flashing("success" -> Messages("groups.created", group.name))
      })
  }

  def show(team: Long, id: Long) = Action { implicit request =>
    Teams.findById(team).map { _ =>
      Groups.findById(id).map { group =>
        Ok(views.html.groups.show(team, group, Accounts.findByTeamAndGroup(team, id)))
      }.getOrElse(NotFound)
    }.getOrElse(NotFound)
  }

  def destroy(id: Long) = Action { implicit request =>
    if (Groups.delete(id)) Ok else NoContent
  }

  def groupForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "description" -> optional(text))(GroupAdd)(GroupAdd.unapply))
}