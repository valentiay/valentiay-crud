package response

import java.util.UUID

import io.circe.{Encoder, Json}
import io.circe.syntax._

case class Response[T](status: Status, time: Long, trace: UUID, load: Either[String, T])

object Response {
  implicit def encodeFoo[T](implicit encoder: Encoder[T]): Encoder[Response[T]] = (r: Response[T]) => Json.obj(
    ("status", r.status.asJson),
    ("time", Json.fromLong(r.time)),
    ("trace", r.trace.asJson),
    ("load", r.load.map(encoder.apply).fold(Json.fromString, identity))
  )
}

sealed trait Status
object Status {
  case object Ok extends Status
  case object Error extends Status

  implicit val encodeEvent: Encoder[Status] = Encoder.instance {
    case Ok => Json.fromString("Ok")
    case Error => Json.fromString("Error")
  }
}
