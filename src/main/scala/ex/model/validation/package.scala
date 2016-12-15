package ex.model

import cats._
import cats.implicits._
import cats.Monad
import ex.model.state.Storage
import ex.model.transaction.Transaction

package object validation {
  def ruleStart[F[_]: Monad: Storage, T <: Transaction](start: Timestamp)(check: T => F[Xor[T]])(t: T): F[Xor[T]] =
    for {
      time <- Storage.lastConfirmedBlockTimestamp()
      r <- if (time >= start)
        Monad[F].pure(Right(t))
      else {
        check(t)
      }
    } yield r
}
