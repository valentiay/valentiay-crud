package services

import cats.syntax.applicative._
import cats.syntax.monadError._
import cats.syntax.functor._
import errors.{FError, InputError}

trait CrudService[F[_]] {
  def opposite(x: Double): F[Double]
  def upper(x: String): F[String]
}

class CrudServiceImpl[F[_]: FError] extends CrudService[F] {
  def opposite(x: Double): F[Double] = x.pure[F].map(x => (100 / (100 * x).toInt).toDouble).adaptError {
    case _ => InputError("double/x", "zero division")
  }

  def upper(x: String): F[String] = x.toUpperCase.pure[F]
}
