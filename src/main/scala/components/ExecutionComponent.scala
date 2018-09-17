package components

import java.util.concurrent.Executors

import config.ExecutionConfig
import monix.execution.Scheduler

class ExecutionComponent(cfg: ExecutionConfig) {
  implicit val scheduler: Scheduler = {
    val javaService = Executors.newScheduledThreadPool(cfg.parallelism)
    Scheduler(javaService)
  }
}
