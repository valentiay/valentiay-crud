package process

import monix.eval.Task
import monix.execution.Scheduler

import scala.concurrent.Future

trait ToFuture[F[_]] {
  def toFuture[T](f: F[T]): Future[T]
}

object ToFuture {
  implicit class Ops[F[_], T](f: F[T]) {
    def toFuture(implicit tf: ToFuture[F]): Future[T] = tf.toFuture(f)
  }

  class fromTask(implicit s: Scheduler) extends ToFuture[Task] {
    override def toFuture[T](f: Task[T]): Future[T] = f.runAsync
  }
}