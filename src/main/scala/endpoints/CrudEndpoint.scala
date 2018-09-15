package endpoints

import io.finch._
import io.finch.syntax.get
import monix.execution.Scheduler
import process.Contextual
import services.CrudService
import response._

class CrudEndpoint(crudService: CrudService[Contextual])(implicit scheduler: Scheduler) {

  val double: CtxEndpoint[Int] = get(path("double") :: path[Int]) {
    x: Int => crudService.double(x)
  }

  val upper: CtxEndpoint[String] = get(path("upper") :: path[String]) {
    x: String => crudService.upper(x)
  }

  val endpoint = double :+: upper
}
