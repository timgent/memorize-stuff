package models

import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._

trait QuestionsModel {
  this: MongoController =>
  def collection: JSONCollection = db.collection[JSONCollection]("questions")
  implicit val questionFormat = Json.format[Question]

  def create = {
    val question = Question("What is a question?","How ironic...", 1)
    val futureResult = collection.insert(question)
    futureResult
  }


}

case class Question(question: String, answer: String, order: Int)