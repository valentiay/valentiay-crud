import com.twitter.util.Future
import io.finch
import io.finch.{Endpoint, Ok}
import io.finch.syntax.{Mapper, ToTwitterFuture}
import monix.execution.Scheduler
import process.{Context, Contextual}
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.applicativeError._
import errors.AppError

package object response {
  type CtxEndpoint[T] = Endpoint[Response[T]]

  implicit def provideContext: () => Context = () => Context.mkContext

  implicit def contextualToTwitterFuture(implicit ctx: () => Context, scheduler: Scheduler): ToTwitterFuture[Contextual] =
    new ToTwitterFuture[Contextual] {
      def apply[A](c: Contextual[A]): Future[A] = finch.syntax.scalaFutures.scalaToTwitterFuture(c.run(ctx()).runAsync)
  }

  def mkErrorResponse[B](ctx: Context)(e: Throwable): Response[B] = e match {
    case err: AppError => Response[B](Status.Error, ctx.time, ctx.trace, Left(err.getMessage))
    case thr: Throwable => Response[B](Status.Error, ctx.time, ctx.trace, Left(s"Unexpected error: ${thr.toString}"))
  }
  def mkOkResponse[B](ctx: Context)(result: B): Response[B] =
    Response[B](Status.Ok, ctx.time, ctx.trace, Right(result))

  implicit def mapperFromContextual[A, B](f: A => Contextual[B])(implicit ttf: ToTwitterFuture[Contextual]): Mapper.Aux[A, Response[B]] =
    Mapper.instance(_.mapOutputAsync(f.andThen(c =>
        for {
          result <- c.attempt
          ctx    <- Contextual.ctx
        } yield Ok(result.fold(mkErrorResponse[B](ctx), mkOkResponse[B](ctx)))
      ).andThen(ttf.apply))
    )
}