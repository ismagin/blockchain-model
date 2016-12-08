package ex.model.transaction

import ex.model._
import ex.model.Currency._

case class IssueTransaction(sender: Address,
                            name: Array[Byte],
                            description: Array[Byte],
                            issue: AssetVolume,
                            decimals: Byte,
                            reissuable: Boolean,
                            feeMoney: WavesVolume,
                            timestamp: Timestamp)
  extends Transaction
