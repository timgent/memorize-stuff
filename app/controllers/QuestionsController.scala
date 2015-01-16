package controllers

import models.QuestionsModel
import play.api.libs.json.Json
import play.api.mvc._
import play.modules.reactivemongo.MongoController
import play.modules.reactivemongo.json.collection.JSONCollection
import play.api.libs.concurrent.Execution.Implicits._

trait QuestionsController extends QuestionsModel with MongoController with Controller {
  this: Controller =>

    def createQuestion = Action {
    Ok(views.html.addQuestion())
  }

  //TODO GET RID OF THIS SHIT
  def addRandomShitToDB = Action {
    create
    Ok("Succesfull insertion, well done")
  }

}

object QuestionsController extends QuestionsController
