package process

import java.util.UUID

case class Context(trace: UUID, time: Long)

object Context {
  def mkContext = Context(UUID.randomUUID, System.currentTimeMillis())
}