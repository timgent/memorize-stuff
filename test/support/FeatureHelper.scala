package support

import models.QuestionsModel
import org.scalatestplus.play.{OneAppPerSuite, OneServerPerSuite}
import play.api.test.FakeApplication
import play.api.test.Helpers._


object FeatureHelper {
  def addQuestionsToDatabase(numberOfQuestions: Int = 10) = {
    1 to 10 by 1 foreach {x =>
      await(QuestionsModel.create("this is the question", "this is the answer"))
    }
  }
}

trait NoAkkaLogging {
  this: OneServerPerSuite =>
  // Override app if you need a FakeApplication with other than
  // default parameters.
  implicit override lazy val app: FakeApplication =
    FakeApplication(
      additionalConfiguration = Map("akka.log-dead-letters" -> "off")
    )
}

trait NoAkkaAppLogging {
  this: OneAppPerSuite =>
  // Override app if you need a FakeApplication with other than
  // default parameters.
  implicit override lazy val app: FakeApplication =
    FakeApplication(
      additionalConfiguration = Map("akka.log-dead-letters" -> "off")
    )
}