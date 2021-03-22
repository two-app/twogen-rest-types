package twogen.types.{{cookiecutter.name}}

import http.ErrorResponse
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._

object Endpoints {
  val health: Endpoint[Unit, ErrorResponse, Unit, Any] =
    endpoint.get.in("health").errorOut(jsonBody[ErrorResponse])
}
