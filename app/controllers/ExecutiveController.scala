package controllers

import javax.inject.{Inject, Singleton}

import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import reactivemongo.api.ReadPreference
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by PE702QE on 01/05/2016.
  */
@Singleton
class ExecutiveController @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents  {
  def ejecutivosFuture: Future[JSONCollection] = database.map(_.collection[JSONCollection]("Ejecutivos"))

  def find = Action.async {
    val cursor: Future[List[JsObject]] = ejecutivosFuture.flatMap{ ejecutivos =>
      ejecutivos.find(Json.obj())
        .sort(Json.obj("documento" -> -1))
        .cursor[JsObject](ReadPreference.primary).collect[List]()
    }
    cursor.map { ejecutivos =>
      Ok(Json.toJson(ejecutivos)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }

  def findById(documentType:String, documentNumber:String) = Action.async {
    val cursor: Future[List[JsObject]] = ejecutivosFuture.flatMap{ ejecutivos =>
      ejecutivos.find(Json.obj("tipo_doc"->documentType, "documento" -> documentNumber)).
        sort(Json.obj("documento" -> -1)).
        cursor[JsObject](ReadPreference.primary).collect[List]()
    }

    cursor.map { ejecutivos =>
      Ok(Json.toJson(ejecutivos)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }
}
