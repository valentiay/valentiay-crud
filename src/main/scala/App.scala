import com.typesafe.config.ConfigFactory
import components.{EndpointComponent, ExecutionComponent, ServiceComponent}
import config.Config
import config.GetConfig.gen

class App {
  val config: Config = gen[Config].get(ConfigFactory.load(), "application")

  val execution = new ExecutionComponent(config.execution)
  val service = new ServiceComponent(config.service)
  val endpoint = new EndpointComponent(execution, service)
  val server = new Server(config.server, endpoint)

  def run(): Unit = server.start()
}
