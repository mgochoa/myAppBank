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

/**
  * A bit more complex controller using a Json Coast-to-coast approach. There is no model for Person and some data is created dynamically on creation
  * Input is directly converted to JsObject to be stored in MongoDB
  */
@Singleton
class ClientController @Inject()(val reactiveMongoApi: ReactiveMongoApi)(implicit exec: ExecutionContext) extends Controller with MongoController with ReactiveMongoComponents {

  def clientesFuture: Future[JSONCollection] = database.map(_.collection[JSONCollection]("Clientes"))

    def find = Action.async {
    // let's do our query
        val cursor: Future[List[JsObject]] = clientesFuture.flatMap{ clientes => //Logger.debug(clientes)
          // find all people with name `name`
          clientes.find(Json.obj())
          .sort(Json.obj("numeroDocumento" -> -1))
          // perform the query and get a cursor of JsObject
          .cursor[JsObject](ReadPreference.primary).collect[List]()
      }
      cursor.map { clientes =>
          Ok(Json.toJson(clientes))
        }
    }
    def create(NombreCompleto: String,tipoDocumento: String, numeroDocumento:Int,ejecutivoAcargo:String) = Action.async {
    val json = Json.obj(
      "NombreCompleto" -> NombreCompleto,
      "tipoDocumento" -> tipoDocumento,
      "numeroDocumento" -> numeroDocumento,
      "ejecutivoAcargo" -> ejecutivoAcargo,
      "productos" -> "[]")

    for {
      clientes <- clientesFuture
      lastError <- clientes.insert(json)
    } yield Ok("Mongo LastError: %s".format(lastError))

  }
  
    def findById(tipoDocumento:String,numeroDocumento: Int) = Action.async {
    // let's do our query
    val cursor: Future[List[JsObject]] = clientesFuture.flatMap{ clientes =>
      // find all people with name `name`
      clientes.find(Json.obj("tipoDocumento"->tipoDocumento,"numeroDocumento" -> numeroDocumento)).
      // sort them by creation date
      sort(Json.obj("numeroDocumento" -> -1)).
      // perform the query and get a cursor of JsObject
      cursor[JsObject](ReadPreference.primary).collect[List]()
  }

    // everything's ok! Let's reply with a JsValue
    cursor.map { clientes =>
      Ok(Json.toJson(clientes))
    }
  }
    
    
}