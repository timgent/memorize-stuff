package functional

import functional.support.NoAkkaLogging
import models.QuestionsModel
import org.scalatest._
import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._

import scala.concurrent.ExecutionContext.Implicits.global

class AddQuestionFeature extends FeatureSpec with GivenWhenThen with MustMatchers with OptionValues
with WsScalaTestClient with OneServerPerSuite with HtmlUnitFactory with OneBrowserPerSuite
with BeforeAndAfter with NoAkkaLogging {
  

  before{
    QuestionsModel.collection.drop()
  }


  scenario("A user is able to add pairs of questions and answers") {
    Given("A user is on the home page")
    go to (s"http://localhost:$port/")
    eventually(pageTitle mustBe "Memorize Stuff - Welcome!")

    When("they navigate to the add question page")
    click on linkText("Add Questions")

    Then("they are able to add in multiple questions and answers")
    addQuestion("Does this functionality work?","Of course it does!")
    eventually(assert(pageSource.contains("Question added!")))
    addQuestion("Question 2", "Answer 2")
    eventually(assert(await(QuestionsModel.findQuestionById(0)).get.question == "Does this functionality work?"))
    assert(await(QuestionsModel.findQuestionById(1)).get.answer == "Answer 2")
  }

  def addQuestion(question: String, answer: String) = {
    textArea("question").value = question
    textArea("answer").value = answer
    submit()
  }
}