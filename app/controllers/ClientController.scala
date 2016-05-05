package controllers

import javax.inject._

import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._
import play.api.mvc._
import play.modules.reactivemongo._
import reactivemongo.api.ReadPreference
import reactivemongo.play.json._
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ClientController @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents {

  def clientesFuture: Future[JSONCollection] = database.map(_.collection[JSONCollection]("Clientes"))

    def find = Action.async {
      val cursor: Future[List[JsObject]] = clientesFuture.flatMap{ clientes => 
        clientes.find(Json.obj())
          .sort(Json.obj("documento" -> -1))
          .cursor[JsObject](ReadPreference.primary).collect[List]()
      }
      cursor.map { clientes =>
        Ok(Json.toJson(clientes)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
      }
    }

    def create = Action.async { request => 
      
      val json = Json.obj(
        "nombre_completo" -> request.body.asFormUrlEncoded.get("nombre_completo")(0),
        "tipo_doc" -> request.body.asFormUrlEncoded.get("tipo_doc")(0),
        "documento" -> request.body.asFormUrlEncoded.get("documento")(0),
        "correo" -> request.body.asFormUrlEncoded.get("correo")(0),
        "ejecutivo_encargado" -> request.body.asFormUrlEncoded.get("ejecutivo_encargado")(0),
        "productos" -> "[]"
      )

      for {
        clientes <- clientesFuture
        lastError <- clientes.insert(json)
      } yield Ok("Mongo LastError: %s" . format(lastError))

  }


  def findById(documentType:String, documentNumber:String) = Action.async {
    Logger.info(documentType);
    val cursor: Future[List[JsObject]] = clientesFuture.flatMap{ clientes =>
      // find all people with name `name`
      clientes.find(Json.obj("tipo_doc" -> documentType, "documento" -> documentNumber)).
      // sort them by creation date
      sort(Json.obj("documento" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject](ReadPreference.primary).collect[List]()
    }

    cursor.map { clientes =>
      Ok(Json.toJson(clientes)).withHeaders(ACCESS_CONTROL_ALLOW_ORIGIN -> "*")
    }
  }

}
