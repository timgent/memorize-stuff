package functional

import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._

class addQuestionSpec extends PlaySpec with OneServerPerSuite with HtmlUnitFactory with OneBrowserPerSuite {
    "A user is able to add pairs of questions and answers" must {
      "Allow a user to add multiple pairs of questions and answers and then take a test on them - " in {
        go to s"http://localhost:$port/"
        eventually { pageTitle mustBe "Memorize Stuff - Welcome!" }
//        click on find(name("Create Question")).value
//        eventually { assert (pageTitle == "Memorize Stuff - Add Question and Answer") }
    }
  }
}