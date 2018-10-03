package services

import java.util.UUID

import cats.syntax.applicative._
import cats.syntax.monadError._
import cats.syntax.functor._
import errors.{FError, InputError}
import io.circe.generic.JsonCodec
import masking.semimasked
import services.CrudService.{Foo, Opposite}

trait CrudService[F[_]] {
  def opposite(x: Double): F[Opposite]
  def upper(x: String): F[String]
}

object CrudService{
  @JsonCodec case class Foo(value: Double)
  @JsonCodec case class Opposite(value: Foo, @semimasked uuid: UUID = UUID.randomUUID())
}


class CrudServiceImpl[F[_]: FError] extends CrudService[F] {
  def opposite(x: Double): F[Opposite] = x.pure[F].map(x => Opposite(Foo((100 / (100 * x).toInt).toDouble))).adaptError {
    case _ => InputError("double/x", "zero division")
  }

  def upper(x: String): F[String] = x.toUpperCase.pure[F]
}
