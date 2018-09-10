package services

import cats.Applicative
import io.finch._
import io.finch.syntax._
import io.finch.syntax.scalaFutures._
import cats.syntax.applicative._
import cats.syntax.functor._
import process.ToFuture
import process.ToFuture.Ops

trait CrudService[F[_]] extends Service[Int]

class CrudServiceImpl[F[_]: Applicative: ToFuture] extends CrudService[F] {

  def double(x: Int): F[Int] = (x * 2).pure[F]
  def div: Endpoint[Int] = get("foo")(double(10).map(r => Ok(r)).toFuture)

  override def endpoints: Endpoint[Int] = div
}
