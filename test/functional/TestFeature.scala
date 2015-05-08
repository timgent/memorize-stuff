package functional

import models.QuestionsModel
import org.scalatest._
import org.scalatestplus.play._
import play.api.libs.json.Json
import play.api.test.Helpers._
import support.{FeatureHelper, NoAkkaLogging}

import scala.concurrent.ExecutionContext.Implicits.global

class TestFeature extends FeatureSpec with GivenWhenThen with MustMatchers with OptionValues
with WsScalaTestClient with OneServerPerSuite with HtmlUnitFactory with OneBrowserPerSuite
with BeforeAndAfter with NoAkkaLogging {

  before {
    await(QuestionsModel.collection.remove(Json.obj()))
    FeatureHelper.addQuestionsToDatabase()
  }

  scenario("A user is able to able to take a test") {
    Given("A user navigates to the test me page")
    go to (s"http://localhost:$port/")
    click on linkText("Test Me!")

    Then("they can see the question but not the answer")
    eventually(assert(pageSource.contains("this is the question")))
    assert(!pageSource.contains("this is the answer"))

    When("the user selects to show the answer")
    click on linkText("Show answer")

    Then("the user can mark whether they are right or wrong")
    assert(pageSource.contains("I'm right!"))
    assert(pageSource.contains("I'm wrong :("))

    When("the user confirms they are right")
    clickOn("right")

    And("then completed another 5 questions incorrectly")
    answerQuestions(5, false)

    And("another 4 questions correctly")
    answerQuestions(4, true)

    Then("the user is shown their score is 5 out of 10")
    eventually(assert(pageSource.contains("5/10")))
  }

  def answerQuestions(numberOfQuestions: Int, answerCorrectly: Boolean) = {
    1 to numberOfQuestions foreach { x =>
      eventually(click on linkText("Show answer"))
      if (answerCorrectly) {
        eventually(clickOn("right"))
      } else {
        eventually(clickOn("wrong"))
      }
    }
  }
}

