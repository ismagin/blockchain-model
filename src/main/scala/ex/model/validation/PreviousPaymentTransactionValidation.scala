package ex.model.validation

import cats._
import cats.implicits._
import cats.Monad
import ex.model._
import ex.model.state.Storage
import ex.model.transaction.{PaymentTransaction, Transaction}

object PreviousPaymentTransactionValidation {
  def apply[F[_]: Storage: Monad](startTime: Timestamp)(t: PaymentTransaction): F[Xor[PaymentTransaction]] =
    ruleStart(startTime)(p[F])(t)

  def p[F[_]: Storage: Monad](t: PaymentTransaction): F[Xor[PaymentTransaction]] =
    for {
      maybeLastTx <- Storage.previousPaymentTransactionTimestamp[F](t.sender)
    } yield
      maybeLastTx match {
        case Some(lastTx) if lastTx >= t.timestamp =>
          Left("Transaction timestamp is in the past")
        case _ => Right(t)
      }

}
