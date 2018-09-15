import components.EndpointComponent
import io.finch.Application
import com.twitter.finagle.Http
import com.twitter.util.Await
import io.finch.circe._
import io.circe.generic.auto._

class Server(endpoint: EndpointComponent) {
  def start(): Unit = Await.ready(Http.server.serve(":8080", endpoint.endpoint.toServiceAs[Application.Json]))
}
