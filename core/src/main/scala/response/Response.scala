package response

import java.util.UUID

import cats.Show
import cats.syntax.show._
import io.circe.{Encoder, Json}
import io.circe.syntax._
import masking._

sealed trait Response[T]{
  val time: Long
  val trace: UUID
}

object Response {
  case class Ok[T](time: Long, trace: UUID, load: T) extends Response[T]
  case class Error[T](time: Long, trace: UUID, load: String) extends Response[T]

  implicit def encodeResponse[T](implicit encoder: Encoder[T]): Encoder[Response[T]] = {
    case Ok(time, trace, load) => Json.obj(
        ("status", Json.fromString("Ok")),
        ("time", Json.fromLong(time)),
        ("trace", trace.asJson),
        ("load", load.asJson(encoder))
      )

    case Error(time, trace, load) => Json.obj(
        ("status", Json.fromString("Error")),
        ("time", Json.fromLong(time)),
        ("trace", trace.asJson),
        ("load", load.asJson)
      )
  }

  private def showWithStatus[T: Show](status: String, time: Long, trace: UUID, load: T): String =
    s"""|status=$status
        |time=$time
        |trace=$trace
        |load=${load.show}
    """.stripMargin

  implicit def showResponse[T: Show]: Show[Response[T]] = {
    case Ok(time, trace, load: T) => showWithStatus[T]("Ok", time, trace, load)
    case Error(time, trace, load) => showWithStatus("Error", time, trace, load)
  }
}
