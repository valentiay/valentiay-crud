package components

import java.util.concurrent.Executors

import monix.execution.Scheduler

class ExecutionComponent {
  implicit val scheduler: Scheduler = {
    val javaService = Executors.newScheduledThreadPool(10)
    Scheduler(javaService)
  }
}
