import com.typesafe.scalalogging.Logger
import process.Contextual
import cats.syntax.functor._

package object logging {
  def logDebug[F[_]](msg: String)(implicit logging: Logging[F], logger: Logger): F[Unit] = logging.debug(msg)
  def logInfo[F[_]](msg: String)(implicit logging: Logging[F], logger: Logger): F[Unit] = logging.info(msg)
  def logWarn[F[_]](msg: String)(implicit logging: Logging[F], logger: Logger): F[Unit] = logging.warn(msg)
  def logError[F[_]](msg: String)(implicit logging: Logging[F], logger: Logger): F[Unit] = logging.error(msg)

  implicit object ContextualLogging extends Logging[Contextual]{
    def wrapMsg(msg: String): Contextual[String] = Contextual.ctx.map { ctx =>
     s"""
        |L trace=${ctx.trace}
        |L spent=${System.currentTimeMillis - ctx.time}
        |$msg
        |""".stripMargin
    }

    def info(msg: String)(implicit logger: Logger): Contextual[Unit] = wrapMsg(msg).map(str => logger.info(str))
    def debug(msg: String)(implicit logger: Logger): Contextual[Unit] = wrapMsg(msg).map(str => logger.debug(str))
    def warn(msg: String)(implicit logger: Logger): Contextual[Unit] = wrapMsg(msg).map(str => logger.warn(str))
    def error(msg: String)(implicit logger: Logger): Contextual[Unit] = wrapMsg(msg).map(str => logger.error(str))
  }
}
