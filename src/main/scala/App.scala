import components.{EndpointComponent, ExecutionComponent, ServiceComponent}

class App {
  val execution = new ExecutionComponent
  val service = new ServiceComponent
  val endpoint = new EndpointComponent(execution, service)
  val server = new Server(endpoint)

  def run(): Unit = server.start()
}
