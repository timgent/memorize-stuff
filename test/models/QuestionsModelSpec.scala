package models

import org.scalatest.BeforeAndAfter
import org.scalatestplus.play.{OneAppPerSuite, PlaySpec}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.test.Helpers._

class QuestionsModelSpec extends PlaySpec with OneAppPerSuite with BeforeAndAfter {
  before {
    QuestionsModel.collection.drop()
  }
  
  "create question" should {
    "create the next question in the sequence" in {
      await(QuestionsModel.create("Q1", "A1"))
      await(QuestionsModel.create("Q2", "A2"))
      await(QuestionsModel.create("Q3", "A3"))

      await(QuestionsModel.findQuestionById(0)).get.question must be("Q1")
      await(QuestionsModel.findQuestionById(1)).get.question must be("Q2")
      await(QuestionsModel.findQuestionById(2)).get.question must be("Q3")
    }
  }
}
