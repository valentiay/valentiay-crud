package components

import endpoints.CrudEndpoint

class EndpointComponent(service: ServiceComponent) {
  val crud = new CrudEndpoint(service.crud)

  val all = crud.endpoint
}
