import cats.MonadError

package object errors {
  type FError[F[_]] = MonadError[F, Throwable]
}
