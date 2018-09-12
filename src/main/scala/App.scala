import java.util.UUID

import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.util.Await
import io.finch._
import io.finch.syntax._
import io.finch.syntax.scalaFutures._
import services.CrudServiceImpl
import cats.instances.int._
import monix.eval.Task
import monix.execution.Scheduler.Implicits.global
import process.{Context, Contextual}
import com.twitter.util.Future
import components.{EndpointComponent, ExecutionComponent, ServiceComponent}

class App {
  val execution = new ExecutionComponent
  val service = new ServiceComponent
  val endpoint = new EndpointComponent(service)

  def run: ListeningServer = Await.ready(Http.server.serve(":8080", endpoint.all.toServiceAs[Text.Plain]))
}
