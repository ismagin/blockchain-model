package ex.model.validation

import cats.data.{NonEmptyList, Validated, ValidatedFunctions}
import cats.free.Free
import ex.model.state.Storage._
import ex.model._

object PaymentTransactionValidation extends ValidatedFunctions {
  def apply(address: Address, ruleStartTime: Timestamp)(paymentTransaction: PaymentTransaction) =
    for {
      time        <- lastConfirmedBlockTimestamp()
      maybeLastTx <- lastPaymentTransactionTimestamp(address)
      r <- maybeLastTx match {
        case Some(lastTx) if lastTx >= paymentTransaction.timestamp && time > ruleStartTime =>
          invalidNel("The transaction timestamp is in the past")
        case _ => valid(paymentTransaction)
      }
    } yield r

}
