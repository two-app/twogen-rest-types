package http

import cats.implicits._
import cats.syntax.either._
import config.PrettyProduct
import io.circe.generic.JsonCodec
import io.circe.parser._
import org.http4s.headers.Authorization
import pdi.jwt.{Jwt, JwtOptions}

@JsonCodec case class User(uid: Int, pid: Int, cid: Int) extends PrettyProduct

object User {

  val authError: ErrorResponse = AuthorizationError("Invalid token.")

  def from(header: Authorization): Either[ErrorResponse, User] = {
    val user = for {
      token <- extractToken(header)
      user <- from(token)
    } yield user
    return user
  }

  def from(accessToken: String): Either[ErrorResponse, User] = {
    for {
      content <- Jwt
        .decode(
          accessToken,
          JwtOptions(signature = false, expiration = false, notBefore = false)
        )
        .toEither
        .map(_.content)
        .leftMap(_ => InternalError())

      parsedContent <- parse(content).leftMap(_ => authError)

      userContent <- parsedContent.as[User].leftMap(_ => authError)
    } yield userContent
  }

  private def extractToken(
      header: Authorization
  ): Either[ErrorResponse, String] = {
    Right(header.value)
      .filterOrElse(t => !t.isBlank, authError)
      .map(header => header.split(" "))
      .filterOrElse(
        parts => parts.length == 2 && parts(0) == "Bearer",
        authError
      )
      .map(parts => parts(1))
  }
}
