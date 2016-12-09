package ex.model.validation

import cats.data.Validated._
import ex.model._
import ex.model.state.Storage._
import ex.model.transaction.PaymentTransaction

object PreviousPaymentTransactionValidation {
  def apply(startTime: Timestamp)(paymentTransaction: PaymentTransaction): FreeValidationResult[PaymentTransaction] =
    for {
      time <- lastConfirmedBlockTimestamp()
      r <- if (time >= startTime)
        lift(valid(paymentTransaction))
      else {
        for {
          maybeLastTx <- previousPaymentTransactionTimestamp(paymentTransaction.sender)
        } yield
          maybeLastTx match {
            case Some(lastTx) if lastTx >= paymentTransaction.timestamp =>
              invalidNel("Transaction timestamp is in the past")
            case _ => valid(paymentTransaction)
          }

      }
    } yield r
}
