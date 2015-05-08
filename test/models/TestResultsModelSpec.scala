package models

import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import play.api.libs.json.Json
import play.api.test.Helpers._
import support.NoAkkaAppLogging

import scala.concurrent.ExecutionContext.Implicits.global

class TestResultsModelSpec extends PlaySpec with OneAppPerSuite with BeforeAndAfter with NoAkkaAppLogging {
  before {
    await(TestResultsModel.collection.remove(Json.obj()))
  }

  def load2Correct1Wrong = {
    TestResultsModel.create(TestResult(1, true))
    TestResultsModel.create(TestResult(2, false))
    TestResultsModel.create(TestResult(3, true))
  }

  "countCorrect" should {
    "count only correct answers" in {
      load2Correct1Wrong
      await(TestResultsModel.countCorrect()) mustEqual 2
    }
  }
  "countWrong" should {
    "count only incorrect answers" in {
      load2Correct1Wrong
      await(TestResultsModel.countWrong()) mustEqual 1
    }
  }
}
