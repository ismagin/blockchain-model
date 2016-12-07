package ex.model.validation

import cats.data.ValidatedFunctions
import ex.model.state.Storage
import ex.model._

object PaymentTransactionValidation extends ValidatedFunctions with Storage {

//  If transaction type is PaymentTransaction transaction creation time should be more then
//  creation time of the last transaction of the same type on payer's account.
//  This rule works only after 1477958400000 on Testnet and after 1479168000000 on Mainnet.

  def againstState(address: Address, ruleStartTime: Timestamp)(
      paymentTransaction: PaymentTransaction) =
    for {
      time        <- timeNow()
      maybeLastTx <- lastPaymentTransactionTimestamp(address)
      r = maybeLastTx match {
        case Some(lastTx)
            if lastTx >= paymentTransaction.timestamp && time > ruleStartTime =>
          invalidNel("")
        case _ => valid(paymentTransaction)
      }
    } yield r

  /*
   Amount is positive, otherwise NegativeAmount validation result is returned.
   Transaction's fee is positive, in other case InsufficientFee validation result is returned.
   Adding fee to amount does not lead to Long overflow. In case of Long overflow OverflowError validation result will be returned.
   Transaction's signature is valid. If not, InvalidSignature is returned.
   */
  def againstContent(chainId: Byte)(paymentTransaction: PaymentTransaction) = {
    ???
  }
}
