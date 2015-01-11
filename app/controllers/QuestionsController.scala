package controllers

import play.api.mvc._

trait QuestionsController {
  this: Controller =>

  def createQuestion = Action {
    Ok(views.html.addQuestion())
  }
}

object QuestionsController extends Controller with QuestionsController