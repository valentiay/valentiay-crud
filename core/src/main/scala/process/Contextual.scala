package process

import cats.{Monad, MonadError}
import errors.FError
import monix.eval.Task
import cats.syntax.applicativeError._

final case class Contextual[A](f: Context => Task[A]) {
  def run(ctx: Context) = f(ctx)
}

object Contextual {
  def ctx = Contextual(ctx => Task.pure(ctx))

  implicit object ContextualMonad extends MonadError[Contextual, Throwable] {
    def pure[A](a: A): Contextual[A] = Contextual(_ => Task.pure(a))

    def flatMap[A, B](fa: Contextual[A])(f: A => Contextual[B]): Contextual[B] = Contextual(ctx => fa.run(ctx).flatMap(a => f(a).run(ctx)))

    override def map[A, B](fa: Contextual[A])(f: A => B): Contextual[B] = Contextual(ctx => fa.run(ctx).map(f))

    def tailRecM[A, B](a: A)(f: A => Contextual[Either[A, B]]): Contextual[B] = Contextual(ctx => Task.tailRecM(a)(x => f(x).run(ctx)))

    def raiseError[A](e: Throwable): Contextual[A] = Contextual(_ => Task.raiseError(e))

    def handleErrorWith[A](fa: Contextual[A])(f: Throwable => Contextual[A]):Contextual[A] = Contextual(ctx => fa.run(ctx).handleErrorWith(f.andThen(_.run(ctx))))
  }
}