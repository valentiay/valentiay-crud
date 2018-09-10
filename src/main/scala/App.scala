import com.twitter.finagle.{Http, ListeningServer}
import com.twitter.util.Await
import io.finch._
import services.CrudServiceImpl
import cats.instances.int._
import monix.eval.Task
import process.ToFuture
import monix.execution.Scheduler.Implicits.global

class App {
  implicit val toFutureFromTask: ToFuture.fromTask = new ToFuture.fromTask

  val endpoint: Endpoint[Int] =
    new CrudServiceImpl[Task].endpoints

  def run: ListeningServer = Await.ready(Http.server.serve(":8080", endpoint.toServiceAs[Text.Plain]))
}
