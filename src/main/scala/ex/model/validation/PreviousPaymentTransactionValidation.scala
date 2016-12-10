package ex.model.validation

import cats.data.Validated._
import ex.model._
import ex.model.state.Storage._
import ex.model.transaction.PaymentTransaction

object PreviousPaymentTransactionValidation {
  def apply(startTime: Timestamp)(t: PaymentTransaction): FreeValidationResult[PaymentTransaction] =
    for {
      time <- lastConfirmedBlockTimestamp()
      r <- if (time >= startTime)
        pure(valid(t))
      else {
        for {
          maybeLastTx <- previousPaymentTransactionTimestamp(t.sender)
        } yield
          maybeLastTx match {
            case Some(lastTx) if lastTx >= t.timestamp =>
              invalidNel("Transaction timestamp is in the past")
            case _ => valid(t)
          }

      }
    } yield r
}
