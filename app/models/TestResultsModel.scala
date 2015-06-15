package models

import org.joda.time.DateTime
import play.api.libs.json.{JsBoolean, Json}
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._
import play.modules.reactivemongo.ReactiveMongoPlugin._
import play.api.Play.current
import reactivemongo.core.commands.Count
import scala.concurrent._
import reactivemongo.bson.{BSONDocument, BSONObjectID}

import scala.util.Random

object TestResultsModel {
  def collection: JSONCollection = db.collection[JSONCollection]("testResults")

  implicit val testResultFormat = Json.format[TestResult]

  def create(testResult: TestResult) = {
    collection.insert(testResult)
  }
  
  def count() = {
    collection.db.command(Count(collection.name))
  }

  def countCorrect() = {
    val query = BSONDocument("isCorrect" -> true)
    collection.db.command(Count(collection.name, Some(query)))
  }

  def countWrong() = {
    val query = BSONDocument("isCorrect" -> false)
    collection.db.command(Count(collection.name, Some(query)))
  }
}


case class TestResult(questionId: Int, isCorrect: Boolean, date: DateTime)