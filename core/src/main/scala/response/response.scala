import cats.Show
import com.twitter.util.Future
import io.finch
import io.finch.{Endpoint, Output}
import io.finch.syntax.{Mapper, ToTwitterFuture}
import monix.execution.Scheduler
import process.{Context, Contextual}
import cats.syntax.applicative._
import cats.syntax.applicativeError._
import cats.syntax.functor._
import cats.syntax.flatMap._
import cats.syntax.show._
import com.typesafe.scalalogging.Logger
import errors.AppError
import logging._
import response.Response.{Error, Ok}
import shapeless.ops.function.FnToProduct

import scala.annotation.implicitNotFound

package object response {
  type CtxEndpoint[T] = Endpoint[Response[T]]

  implicit def provideContext: () => Context = () => Context.mkContext

  implicit def contextualToTwitterFuture(implicit ctx: () => Context, scheduler: Scheduler): ToTwitterFuture[Contextual] =
    new ToTwitterFuture[Contextual] {
      def apply[A](c: Contextual[A]): Future[A] = finch.syntax.scalaFutures.scalaToTwitterFuture(c.run(ctx()).runAsync)
  }

  private def mkErrorResponse[B](ctx: Context)(e: Throwable): Error[B] = e match {
    case err: AppError => Error(ctx.time, ctx.trace, err.getMessage)
    case thr: Throwable => Error(ctx.time, ctx.trace, s"Unexpected error: ${thr.toString}")
  }
  private def mkOkResponse[B](ctx: Context)(result: B): Ok[B] =
    Ok(ctx.time, ctx.trace, result)

  private def logException(implicit logger: Logger): PartialFunction[Throwable, Contextual[Unit]] = {
    case err: AppError => ().pure[Contextual]
    case thr: Throwable => logWarn(s"Unexpected error: ${thr.toString}")
  }

  private def wrapLogic[A: Show, B: Show](a: A, b: Contextual[B])(implicit logger: Logger): Contextual[Output[Response[B]]] = for {
    _ <- logInfo(show"Received request:\n$a")
    result <- b.attempt
    ctx <- Contextual.ctx
    res = result.fold(mkErrorResponse[B](ctx), mkOkResponse[B](ctx))
    _ <- logInfo(show"Sending response:\n$res")
  } yield io.finch.Ok(res)


  @implicitNotFound("Ensure cats.Show and Logger instances privided. You can import masking._ and logging._")
  implicit def mapperFromContextualHFunction[A, B, F, FB](f: F)(implicit logger: Logger,
                                                                         ftp: FnToProduct.Aux[F, A => FB],
                                                                         ev: FB <:< Contextual[B],
                                                                         ttf: ToTwitterFuture[Contextual],
                                                                         showA: Show[A],
                                                                         showB: Show[B]
  ): Mapper.Aux[A, Response[B]] = Mapper.instance(_.mapOutputAsync(req => ttf(wrapLogic(req, ev(ftp(f)(req))))))

  @implicitNotFound("Ensure cats.Show and Logger instances privided. You can import masking._ and logging._")
  implicit def mapperFromContextual[A: Show, B: Show](f: A => Contextual[B])(implicit ttf: ToTwitterFuture[Contextual],
                                                                            logger: Logger
  ): Mapper.Aux[A, Response[B]] = Mapper.instance(_.mapOutputAsync(req => ttf(wrapLogic(req, f(req)))))

}