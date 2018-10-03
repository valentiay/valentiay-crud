package endpoints

import com.typesafe.scalalogging.Logger
import io.finch._
import io.finch.syntax.get
import monix.execution.Scheduler
import process.Contextual
import services.CrudService
import response._
import masking._
import services.CrudService.Opposite

class CrudEndpoint(crudService: CrudService[Contextual])(implicit scheduler: Scheduler) {
  implicit val logger: Logger = Logger[CrudEndpoint]

  val double: CtxEndpoint[Opposite] = get(path("opposite") :: path[String] :: path[Int]) { (x: String, y: Int) =>
      crudService.opposite(x.toDouble)
  }

  val upper: CtxEndpoint[String] = get(path("upper") :: path[String]) {
    x: String => crudService.upper(x)
  }

  val endpoint = double :+: upper
}
