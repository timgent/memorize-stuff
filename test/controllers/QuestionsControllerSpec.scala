package controllers

import scala.concurrent.Future
import org.scalatestplus.play._
import play.api.mvc._
import play.api.test._
import play.api.test.Helpers._

class QuestionsControllerSpec extends PlaySpec with OneAppPerSuite {
  
  class TestQuestionsController() extends Controller with QuestionsController

  "createQuestion" must {

    "direct to the create question page" in {
      val controller = new TestQuestionsController()
      val result: Future[Result] = controller.createQuestionView().apply(FakeRequest())
      val bodyText: String = contentAsString(result)
      bodyText must include("Add Question and Answer")
    }
  }
}