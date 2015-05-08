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
  val ID = "_id"

  def create(question: String, answer: String) = {
    for {questionId <- getNextQuestionId} yield collection.insert(Question(question, answer, questionId))
  }

  def findRandomQuestion: Future[Option[Question]] = {
    val rand = getNextQuestionId.flatMap {totalQs =>
      if (totalQs>0) {
        collection.find(Json.obj(ID -> Random.nextInt(totalQs))).one[Question]
      } else {Future(None)}
    }
    rand
  }

  def findQuestionById(_id: Int): Future[Option[Question]] = {
    collection.find(Json.obj(ID -> _id)).one[Question]
  }

//  TODO: Remove this abomination, won't need it once I have a new way to select random questions
  def getNextQuestionId: Future[Int] = {
    db.command(Count("questions", None, None))
  }

  def totalNumberOfQuestions: Future[Int] = {
    getNextQuestionId.map(_ - 1)
  }

}

object QuestionsModel extends QuestionsModel

case class Question(question: String, answer: String, _id: Int)
