package ex.model

import cats.data.ValidatedFunctions
import cats.free._
import cats.syntax.all._
import ex.model.state.Storage


object PaymentTransactionValidation extends ValidatedFunctions with Storage {

//  If transaction type is PaymentTransaction transaction creation time should be more then
//  creation time of the last transaction of the same type on payer's account.
//  This rule works only after 1477958400000 on Testnet and after 1479168000000 on Mainnet.

  def validate(account: Account, ruleStartTime: Timestamp, paymentTransaction: PaymentTransaction) =
    for {
      time        <- timeNow()
      maybeLastTx <- lastPaymentTransactionTimestamp(account)
      r = maybeLastTx match {
        case Some(lastTx) if lastTx >= paymentTransaction.timestamp && time > ruleStartTime => invalidNel("")
        case _                                                                              => valid(paymentTransaction)
      }
    } yield r

}
