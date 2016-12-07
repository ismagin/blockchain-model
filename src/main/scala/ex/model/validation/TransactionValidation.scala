package ex.model.validation

import cats.data.ValidatedFunctions
import ex.model._
import ex.model.state.Storage

object TransactionValidation extends ValidatedFunctions with Storage {

//  Transaction creation time more then block's creation time no more then
//  on MaxTimeForUnconfirmed (90 minutes). This limitation works always on Testnet
//  and only after 1479168000000 on Mainnet.
  private val MaxTimeForUnconfirmed = 90 * 60 * 1000 // 90 minutes

  def creationTime(ruleStartTime: Timestamp)(t: Transaction) =
    for {
      time <- lastConfirmedBlockTimestamp()
    } yield
      if (time > ruleStartTime || t.timestamp - time > MaxTimeForUnconfirmed)
        invalidNel(
          "Transaction creation time more then block's creation time no more then on MaxTimeForUnconfirmed")
      else valid(t)

//  Application of transaction to accounts should not lead to temporary negative balance.
//  This rule works after 1479168000000 on Mainnet and after 1477958400000 on Testnet.

  def negativeBalance(startTime: Timestamp)(t: SenderRecepientTransaction) = {
    for {
      senderBalance <- accBalance(t.sender)
      recepientBalance <- accBalance(t.recipient)

    } yield 0
  }

}
