package ex.model.validation

import cats.data.{NonEmptyList, Validated, ValidatedFunctions}
import cats.free.Free
import ex.model.state.Storage._
import ex.model._
import ex.model.validation.NegativeBalanceValidation.{invalidNel, valid}

object PaymentTransactionValidation extends ValidatedFunctions {
  def apply(address: Address, startTime: Timestamp)(paymentTransaction: PaymentTransaction): FreeValidationResult[FromToTransaction] =
    for {
      time <- lastConfirmedBlockTimestamp()
      r <- if (time >= startTime)
        lift(valid(paymentTransaction))
      else {
        for {
          maybeLastTx <- lastPaymentTransactionTimestamp(address)
          res <- maybeLastTx match {
            case Some(lastTx) if lastTx >= paymentTransaction.timestamp =>
              invalidNel("The transaction timestamp is in the past")
            case _ => valid(paymentTransaction)
          }
        } yield res
      }
    } yield r
}
