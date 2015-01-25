package controllers

import models.{Question, QuestionsModel}
import play.api.data._
import play.api.data.Forms._
import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._

import scala.concurrent.Future

trait QuestionsController extends QuestionsModel with MongoController with Controller {
  this: Controller =>

  def createQuestionView = Action.async { implicit request =>
    Future(Ok(views.html.createQuestion()))
  }

  val createQuestionForm = Form(
    tuple(
      "question" -> nonEmptyText,
      "answer" -> nonEmptyText
    )
  )

  def createQuestion = Action { implicit request =>
    createQuestionForm.bindFromRequest.fold(
      formWithErrors => {
        // binding failure, you retrieve the form containing errors:
        BadRequest("BAD BAD BAD")
      },
      question => {
        /* binding success, you get the actual value. */
        QuestionsModel.create(question._1, question._2)
      }
    )
    Redirect(controllers.routes.QuestionsController.createQuestionView()).flashing(Flash(Map("message" -> "Question added!")))
  }

  def testMeWithQuestionNumber(questionNumber: String) = Action.async { implicit request =>
    findQuestionById(questionNumber.toInt).map(
      _ match {
        case Some(question) => Ok(views.html.test(question, false))
        case None => Ok("No questions found")
      }
    )
  }

  def testMe() = Action.async { implicit request =>
    findRandomQuestion.map(
      _ match {
        case Some(question) => Redirect(controllers.routes.QuestionsController.testMeWithQuestionNumber(question._id.toString))
        case None => Ok("No questions found")
      }
    )
  }

  def testMeShowAnswer(questionNumber: String) = Action.async { implicit request =>
    val question = findQuestionById(questionNumber.toInt)
    question.map { question =>
      question match {
        case Some(question) => Ok(views.html.test(question, true))
        case None => Ok("Sorry it appears this question no longer exists")
      }
    }
  }

  def checkAnswer = Action { implicit request =>
    Ok("Checking answer...")
  }

  val correctAnswerForm = Form(
    mapping(
      "questionID" -> number,
      "answerBoolean" -> boolean
    )(SubmittedAnswer.apply)(SubmittedAnswer.unapply)
  )

  case class SubmittedAnswer(id: Int, answerBoolean: Boolean)

}

object QuestionsController extends QuestionsController {
}
