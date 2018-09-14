package components

import java.util.UUID

import com.twitter.util.{Future, Promise}
import endpoints.CrudEndpoint
import io.finch.syntax.ToTwitterFuture
import process.{Context, Contextual}
import monix.execution.Scheduler.Implicits.global
import scala.util.{Failure, Success, Try}

class EndpointComponent(service: ServiceComponent) {
  implicit def provideContext: ToTwitterFuture[Contextual] = new ToTwitterFuture[Contextual] {
    def apply[A](c: Contextual[A]): Future[A] = {
      val p = new Promise[A] with (Try[A] => Unit) {
        def apply(ta: Try[A]): Unit = ta match {
          case Success(a) => setValue(a)
          case Failure(t) => setException(t)
        }
      }
      c.run(Context(UUID.randomUUID, System.currentTimeMillis)).runAsync.onComplete(p)
      p
    }
  }

  val crud = new CrudEndpoint(service.crud)

  val all = crud.endpoint
}
