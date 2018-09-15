package services

import cats.Monad
import cats.syntax.applicative._

trait CrudService[F[_]] {
  def double(x: Int): F[Int]
  def upper(x: String): F[String]
}

class CrudServiceImpl[F[_]: Monad] extends CrudService[F] {

  def double(x: Int): F[Int] = (x * 2).pure[F]

  def upper(x: String): F[String] = x.toUpperCase.pure[F]
}
