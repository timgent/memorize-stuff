package controllers

import models.{TestResult, TestResultsModel, Question, QuestionsModel}
import org.joda.time.DateTime
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

  val createQuestionForm = Form(
    tuple(
      "question" -> nonEmptyText,
      "answer" -> nonEmptyText
    )
  )
  val submitAnswerForm = Form(
    tuple(
      "isCorrect" -> boolean,
      "questionId" -> number
    )
  )
  val correctAnswerForm = Form(
    mapping(
      "questionID" -> number,
      "answerBoolean" -> boolean
    )(SubmittedAnswer.apply)(SubmittedAnswer.unapply)
  )

  def createQuestionView = Action.async { implicit request =>
    Future(Ok(views.html.createQuestion()))
  }

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

  def testMeWithQuestionNumber(questionNumber: String, questionsAnswered: String) = Action.async { implicit request =>
    findQuestionById(questionNumber.toInt).map(
      _ match {
        case Some(question) => Ok(views.html.test(question, false, questionsAnswered))
        case None => Ok("No questions found")
      }
    )
  }

  def testMe(questionsAnswered: String = "0") = Action.async { implicit request =>
    findRandomQuestion.map {
      case Some(question) => Redirect(controllers.routes.QuestionsController.testMeWithQuestionNumber(question._id.toString, questionsAnswered))
      case None => Ok("No questions found")
    }
  }

  def randomStuff = {
    Ok("")
  }

  def testMeShowAnswer(questionNumber: String, questionsAnswered: String) = Action.async { implicit request =>
    val question = findQuestionById(questionNumber.toInt)
    question.map {
      case Some(question) => Ok(views.html.test(question, true, questionsAnswered))
      case None => Ok("Sorry it appears this question no longer exists")
    }
  }

//  TODO: Update so it posts questionsAnswered instead of including it as a param
  def checkAnswer(questionsAnswered: String) = Action.async { implicit request =>
    submitAnswerForm.bindFromRequest.fold(
      formWithErrors => {
        Future(Ok("You buggered that one up"))
      },
      answer => {
        TestResultsModel.create(TestResult(answer._2, answer._1, DateTime.now))
        questionsAnswered match {
          case qs if qs.toInt > 8 =>
            val score = TestResultsModel.countCorrect()
            score.map(s => Ok(views.html.results(s.toString)))
          case qs =>
            Future(Redirect(controllers.routes.QuestionsController.testMe((questionsAnswered.toInt + 1).toString)))
        }
      }
    )
  }

  def showResults(score: String) = Action {
    Ok(views.html.results(score))
  }

  case class SubmittedAnswer(id: Int, answerBoolean: Boolean)

}

object QuestionsController extends QuestionsController {
}
