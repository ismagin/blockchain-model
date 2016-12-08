package ex.model.transaction

import ex.model.Currency._
import ex.model._
case class TransferTransaction(timestamp: Timestamp,
                               sender: Address,
                               recipient: Address,
                               quantity: Volume,
                               fee: Volume,
                               attachment: Array[Byte])
    extends FromToTransaction
