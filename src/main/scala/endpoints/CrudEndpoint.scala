package endpoints

import cats.Functor
import cats.syntax.functor._
import io.finch._
import io.finch.syntax._
import services.CrudService

class CrudEndpoint[F[_]: ToTwitterFuture: Functor](crudService: CrudService[F]) {
  val double: Endpoint[Int] = get(path("double") :: path[Int]) {
    x: Int => crudService.double(x).map(Ok)
  }

  val upper: Endpoint[String] = get(path("upper") :: path[String]) {
    x: String => crudService.upper(x).map(Ok)
  }

  val endpoint = double :+: upper
}
