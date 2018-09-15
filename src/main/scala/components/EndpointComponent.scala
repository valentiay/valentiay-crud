package components

import endpoints.CrudEndpoint

class EndpointComponent(execution: ExecutionComponent, service: ServiceComponent) {
  import execution._

  val crud = new CrudEndpoint(service.crud)

  val endpoint = crud.endpoint
}
