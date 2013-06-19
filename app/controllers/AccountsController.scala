package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data._
import play.api.data.Forms._
import models._
import views.html.defaultpages.badRequest

object AccountsController extends Controller {

  val Home = Redirect(routes.TeamsController.index)

  def newPage(team: Long, group: Long) = Action { implicit request =>
    Groups.findById(team).map { group =>
      Ok(views.html.accounts.newPage(group.teamId, group.id.get, accountForm))
    }.getOrElse(NotFound)
  }

  def create(team: Long, group: Long) = Action { implicit request =>
    accountForm.bindFromRequest.fold(
      formWithErrors =>
        BadRequest(views.html.accounts.newPage(team, group, formWithErrors)),
      account => {
        Accounts.create(Account(None, group, account.name, account.username, account.password, account.description))
        Home.flashing("success" -> Messages("accounts.created", account.name))
      })
  }

  def show(team: Long, group: Long, id: Long) = Action { implicit request =>
    Accounts.findById(id).map { account =>
      Ok(views.html.accounts.show(team, group, account))
    }.getOrElse(NotFound)
  }

  def destroy(id: Long) = Action { implicit request =>
    if (Groups.delete(id)) Ok else NoContent
  }

  def accountForm = Form(
    mapping(
      "name" -> nonEmptyText,
      "username" -> nonEmptyText,
      "password" -> nonEmptyText,
      "description" -> optional(text))(AccountAdd)(AccountAdd.unapply))
}