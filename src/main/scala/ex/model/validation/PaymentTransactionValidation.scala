package ex.model.validation

import cats.data.{NonEmptyList, Validated, ValidatedFunctions}
import cats.free.Free
import ex.model.state.Storage
import ex.model._

object PaymentTransactionValidation extends ValidatedFunctions with Storage {

//  If transaction type is PaymentTransaction transaction creation time should be more then
//  creation time of the last transaction of the same type on payer's account.
//  This rule works only after 1477958400000 on Testnet and after 1479168000000 on Mainnet.

  def apply(address: Address, ruleStartTime: Timestamp)(
      paymentTransaction: PaymentTransaction):
  Free[_root_.ex.model.validation.PaymentTransactionValidation.DSL, Validated[NonEmptyList[BlockId], PaymentTransaction]] =
    for {
      time        <- lastConfirmedBlockTimestamp()
      maybeLastTx <- lastPaymentTransactionTimestamp(address)
      r = maybeLastTx match {
        case Some(lastTx)
            if lastTx >= paymentTransaction.timestamp && time > ruleStartTime =>
          invalidNel("")
        case _ => valid(paymentTransaction)
      }
    } yield r

}
