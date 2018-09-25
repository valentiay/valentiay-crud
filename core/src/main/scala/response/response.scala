import com.twitter.util.Future
import io.finch
import io.finch.{Endpoint, Ok}
import io.finch.syntax.{Mapper, ToTwitterFuture}
import monix.execution.Scheduler
import process.{Context, Contextual}
import cats.syntax.applicative._
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.applicativeError._
import com.typesafe.scalalogging.Logger
import errors.AppError

import logging._

package object response {
  type CtxEndpoint[T] = Endpoint[Response[T]]

  implicit def provideContext: () => Context = () => Context.mkContext

  implicit def contextualToTwitterFuture(implicit ctx: () => Context, scheduler: Scheduler): ToTwitterFuture[Contextual] =
    new ToTwitterFuture[Contextual] {
      def apply[A](c: Contextual[A]): Future[A] = finch.syntax.scalaFutures.scalaToTwitterFuture(c.run(ctx()).runAsync)
  }

  private def mkErrorResponse[B](ctx: Context)(e: Throwable): Response[B] = e match {
    case err: AppError => Response[B](Status.Error, ctx.time, ctx.trace, Left(err.getMessage))
    case thr: Throwable => Response[B](Status.Error, ctx.time, ctx.trace, Left(s"Unexpected error: ${thr.toString}"))
  }
  private def mkOkResponse[B](ctx: Context)(result: B): Response[B] =
    Response[B](Status.Ok, ctx.time, ctx.trace, Right(result))

  private def logException(implicit logger: Logger): PartialFunction[Throwable, Contextual[Unit]] = {
    case err: AppError => ().pure[Contextual]
    case thr: Throwable => logWarn(s"Unexpected error: ${thr.toString}")
  }

  implicit def mapperFromContextual[A, B](f: A => Contextual[B])(implicit ttf: ToTwitterFuture[Contextual], logger: Logger): Mapper.Aux[A, Response[B]] =
    Mapper.instance(_.mapOutputAsync(req =>
      f.andThen(c =>
        for {
          _ <- logInfo(s"Recieved request:\n $req")
          result <- c.attempt
          ctx <- Contextual.ctx
          res = result.fold(mkErrorResponse[B](ctx), mkOkResponse[B](ctx))
          _ <- logInfo(s"Sending response:\n $res")
        } yield Ok(res)
      ).andThen(ttf.apply).apply(req)
    ))
}