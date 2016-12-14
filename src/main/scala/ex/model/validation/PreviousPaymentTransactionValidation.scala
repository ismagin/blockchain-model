package ex.model.validation

import cats._
import cats.implicits._
import cats.Monad
import ex.model._
import ex.model.state.Storage
import ex.model.transaction.{PaymentTransaction, Transaction}

object PreviousPaymentTransactionValidation {
  def apply[F[_]: Storage: Monad, T <: Transaction](startTime: Timestamp)(t: PaymentTransaction): F[Xor[PaymentTransaction]] =
    for {
      time <- Storage.lastConfirmedBlockTimestamp()
      r <- if (time >= startTime)
        Monad[F].pure(Right(t))
      else {
        for {
          maybeLastTx <- Storage.previousPaymentTransactionTimestamp(t.sender)
        } yield
          maybeLastTx match {
            case Some(lastTx) if lastTx >= t.timestamp =>
              Left("Transaction timestamp is in the past")
            case _ => Right(t)
          }

      }
    } yield r
}
