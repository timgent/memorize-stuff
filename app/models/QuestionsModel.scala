package models

import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.ReactiveMongoPlugin._
import play.api.Play.current
import reactivemongo.bson.BSONObjectID

trait QuestionsModel {
  def collection: JSONCollection = db.collection[JSONCollection]("questions")

  implicit val questionFormat = Json.format[Question]

  def create(question: String, answer: String) = {
    println("Got here")
    val newQuestion = Question(question, answer)
    val futureResult = collection.insert(newQuestion)
    futureResult
  }
}

object QuestionsModel extends QuestionsModel

case class Question(question: String, answer: String)