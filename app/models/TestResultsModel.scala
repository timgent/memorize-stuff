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

  private def last10Questions() = {
    collection.find(Json.obj()).sort(Json.obj("date" -> -1)).cursor.toList(10)
  }

  def countCorrect(): Future[Int] = {
    last10Questions().map(questions => questions.filter(_.isCorrect).length)
  }

  def countWrong() = {
    countCorrect().map(10 - _)
  }
}


case class TestResult(questionId: Int, isCorrect: Boolean, date: DateTime)