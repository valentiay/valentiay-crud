package process

import cats.Monad
import monix.eval.Task

final case class Contextual[A](f: Context => Task[A]) {
  def run(ctx: Context) = f(ctx)
}

object Contextual {

  implicit object ContextualMonad extends Monad[Contextual] {
    def pure[A](a: A): Contextual[A] = Contextual(_ => Task.pure(a))

    def flatMap[A, B](fa: Contextual[A])(f: A => Contextual[B]): Contextual[B] = Contextual(ctx => fa.run(ctx).flatMap(a => f(a).run(ctx)))

    override def map[A, B](fa: Contextual[A])(f: A => B): Contextual[B] = Contextual(ctx => fa.run(ctx).map(f))

    def tailRecM[A, B](a: A)(f: A => Contextual[Either[A, B]]): Contextual[B] = Contextual(ctx => Task.tailRecM(a)(x => f(x).run(ctx)))

  }

}