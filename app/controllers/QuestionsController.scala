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

  def createQuestionView = Action.async {
    getNextQuestionNumber.map(questionNumber =>
      Ok(views.html.addQuestion(questionNumber))
    )
  }

  val createQuestionForm = Form(
    mapping(
      "question" -> nonEmptyText,
      "answer" -> nonEmptyText,
      "randomIndex" -> number
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
        val newQuestion = models.Question(question.question, question.answer, question.randomIndex)
        QuestionsModel.create(newQuestion)
      }
    )
    Redirect(controllers.routes.QuestionsController.createQuestionView())
  }

  def testMeWithQuestionNumber(questionNumber: String) = Action.async {
    findQuestionByRandomIndex(questionNumber.toInt).map(
      _ match {
        case Some(question) => Ok(views.html.test(question, false))
        case None => Ok("No questions found")
      }
    )
  }

  def testMe() = Action.async {
    findRandomQuestion.map(
      _ match {
        case Some(question) => Redirect(controllers.routes.QuestionsController.testMeWithQuestionNumber(question.randomIndex.toString))
        case None => Ok("No questions found")
      }
    )
  }
  
  def testMeShowAnswer(questionNumber: String) = Action.async {
    val question = findQuestionByRandomIndex(questionNumber.toInt)
    question.map {question =>
      question match {
        case Some(question) => Ok(views.html.test(question, true))
        case None           => Ok("Sorry it appears this question no longer exists")
      }
    }
  }

  def checkAnswer = Action {
    Ok("Checking answer...")
  }

  val correctAnswerForm = Form(
    mapping(
      "questionID" -> number,
      "answerBoolean" -> boolean,
    )(SubmittedAnswer.apply)(SubmittedAnswer.unapply)
  )

  case class (id: String, answerBoolean: Boolean)
}

object QuestionsController extends QuestionsController {
}
