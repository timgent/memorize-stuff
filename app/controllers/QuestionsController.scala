package controllers

import models.{Question, QuestionsModel}
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._

trait QuestionsController extends QuestionsModel with MongoController with Controller {
  this: Controller =>

  def createQuestionView = Action {
    Ok(views.html.addQuestion())
  }

  val createQuestionForm = Form(
    mapping(
      "question" -> nonEmptyText,
    "answer" -> nonEmptyText
    )(Question.apply)(Question.unapply)
  )

  def createQuestion = Action { implicit request =>
    createQuestionForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest("BAD BAD BAD")
      },
      question => {
        /* binding success, you get the actual value. */
        val newQuestion = models.Question(question.question, question.answer)
        QuestionsModel.create(newQuestion.question, newQuestion.answer)
//        val id = models.Question.create(newUser)
//        Redirect(routes.Application.home(id))
      }
    )
    Ok("Well done you've created a question!")
  }


}

object QuestionsController extends QuestionsController
