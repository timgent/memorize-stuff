package controllers

import play.api.mvc._

object Questions extends Controller {

  def createQuestion = Action {
   Ok (views.html.addQuestion())
}
}