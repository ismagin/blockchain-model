package ex.model.transaction

import ex.model._

case class ReissueTransaction(sender: Address,
                              issue: AssetVolume,
                              reissuable: Boolean,
                              feeMoney: WavesVolume,
                              timestamp: Timestamp)
  extends Transaction
