package components

import process.Contextual
import services._

class ServiceComponent {
  val crud: CrudService[Contextual] = new CrudServiceImpl[Contextual]
}
