import components.EndpointComponent
import io.finch.Application
import com.twitter.finagle.Http
import com.twitter.util.Await
import config.ServerConfig
import io.finch.circe._
import io.circe.generic.auto._

class Server(cfg: ServerConfig, endpoint: EndpointComponent) {
  def start(): Unit = Await.ready(Http.server.serve(s":${cfg.port}", endpoint.endpoint.toServiceAs[Application.Json]))
}
