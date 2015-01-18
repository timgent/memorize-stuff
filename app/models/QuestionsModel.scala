package models

import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.ReactiveMongoPlugin._
import play.api.Play.current
import reactivemongo.core.commands.Count
import scala.concurrent._
import reactivemongo.bson.BSONObjectID

import scala.util.Random

trait QuestionsModel {
  def collection: JSONCollection = db.collection[JSONCollection]("questions")

  implicit val questionFormat = Json.format[Question]
  val randomIndex = "randomIndex"

  def create(question: Question) = {
    val futureResult = collection.insert(question)
    futureResult
  }

  def findRandomQuestion: Future[Option[Question]] = {
    val rand = getNextQuestionNumber.flatMap {totalQs =>
      if (totalQs>0) {
        collection.find(Json.obj(randomIndex -> Random.nextInt(totalQs))).one[Question]
      } else {Future(None)}
    }
    rand
  }

  def findQuestionByRandomIndex(index: Int): Future[Option[Question]] = {
    collection.find(Json.obj(randomIndex -> index)).one[Question]
  }
  
  def getNextQuestionNumber: Future[Int] = {
    db.command(Count("questions", None, None))
  }

  def totalNumberOfQuestions: Future[Int] = {
    getNextQuestionNumber.map(_ - 1)
  }

}

object QuestionsModel extends QuestionsModel

case class Question(question: String, answer: String, randomIndex: Int)