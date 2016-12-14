package ex.model.validation

import cats._
import cats.implicits._
import cats.Monad
import com.github.nscala_time.time.Imports._
import ex.model._
import ex.model.state.Storage
import ex.model.transaction.Transaction

object MaxTimeUnconfirmedValidation {

  private val MaxTimeForUnconfirmed = 90.minutes.millis
  def apply[F[_]: Storage: Monad, T <: Transaction](ruleStartTime: Timestamp)(t: T): F[Xor[T]] =
    for {
      time <- Storage.lastConfirmedBlockTimestamp()
    } yield
      if (time > ruleStartTime && t.timestamp - time > MaxTimeForUnconfirmed)
        Left("Transaction creation time more then block's creation time no more then on MaxTimeForUnconfirmed")
      else Right(t)
}
