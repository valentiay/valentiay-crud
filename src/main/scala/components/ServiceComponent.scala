package components

import config.ServiceConfig
import process.Contextual
import services._

class ServiceComponent(cfg: ServiceConfig) {
  val crud: CrudService[Contextual] = new CrudServiceImpl[Contextual]
}
