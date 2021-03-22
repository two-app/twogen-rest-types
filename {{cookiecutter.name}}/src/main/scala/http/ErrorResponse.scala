package http

import config.PrettyProduct
import io.circe.{Decoder, Encoder, HCursor, Json}
import org.http4s.Status

sealed trait ErrorResponse extends Throwable with PrettyProduct {
  def status: Status
  def reason: String
}

object ErrorResponse {

  implicit val errorEncoder: Encoder[ErrorResponse] =
    (e: ErrorResponse) =>
      Json.obj(
        "status" -> Json.fromInt(e.status.code),
        "reason" -> Json.fromString(e.reason)
      )

  implicit val errorDecoder: Decoder[ErrorResponse] =
    (c: HCursor) =>
      for {
        statusCode <- c.downField("status").as[Int]
        reason <- c.downField("reason").as[String]
      } yield {
        Status
          .fromInt(statusCode)
          .map {
            case Status.Unauthorized        => AuthorizationError(reason)
            case Status.InternalServerError => InternalError(reason)
            case Status.BadRequest          => ClientError(reason)
            case Status.NotFound            => NotFoundError(reason)
            case _                          => InternalError(reason)
          }
          .getOrElse(InternalError(reason))
      }

}

final case class HttpError(status: Status, reason: String)

final case class InternalError(reason: String = "Something went wrong.")
    extends ErrorResponse {
  override def status: Status = Status.InternalServerError
}

final case class AuthorizationError(reason: String) extends ErrorResponse {
  override def status: Status = Status.Unauthorized
}

final case class ClientError(reason: String) extends ErrorResponse {
  override def status: Status = Status.BadRequest
}

final case class NotFoundError(reason: String) extends ErrorResponse {
  override def status: Status = Status.NotFound
}
