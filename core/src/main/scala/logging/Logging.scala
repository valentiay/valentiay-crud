package logging

import com.typesafe.scalalogging.Logger

trait Logging[F[_]] {
  def info(msg: String)(implicit logger: Logger): F[Unit]
  def debug(msg: String)(implicit logger: Logger): F[Unit]
  def warn(msg: String)(implicit logger: Logger): F[Unit]
  def error(msg: String)(implicit logger: Logger): F[Unit]
}