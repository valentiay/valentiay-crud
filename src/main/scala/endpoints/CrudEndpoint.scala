package endpoints

import java.util.UUID

import io.finch._
import io.finch.syntax._
import io.finch.syntax.scalaFutures._
import process.{Context, Contextual}
import services.CrudService
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future

class CrudEndpoint(crudService: CrudService[Contextual]) {
  def provideContext[T](c: Contextual[T]): Future[Output[T]] = c.run(Context(UUID.randomUUID, System.currentTimeMillis)).map(x => Ok(x)).runAsync

  val double = get(path("double") :: path[Int]) {
    x: Int => provideContext(crudService.double(x))
  }

  val upper = get(path("upper") :: path[String]) {
    x: String => provideContext(crudService.upper(x))
  }

  val endpoint = double :+: upper
}
