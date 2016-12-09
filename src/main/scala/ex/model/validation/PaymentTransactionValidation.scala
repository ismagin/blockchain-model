package ex.model.validation

import cats.data.ValidatedFunctions
import ex.model._
import ex.model.state.Storage._
import ex.model.transaction.PaymentTransaction

object PaymentTransactionValidation extends ValidatedFunctions {
  def apply(startTime: Timestamp)(paymentTransaction: PaymentTransaction): FreeValidationResult[PaymentTransaction] =
    for {
      time <- lastConfirmedBlockTimestamp()
      r <- if (time >= startTime)
        lift(valid(paymentTransaction))
      else {
        for {
          maybeLastTx <- lastPaymentTransactionTimestamp(paymentTransaction.sender)
          res <- maybeLastTx match {
            case Some(lastTx) if lastTx >= paymentTransaction.timestamp =>
              invalidNel("Transaction timestamp is in the past")
            case _ => valid(paymentTransaction)
          }
        } yield res
      }
    } yield r
}
