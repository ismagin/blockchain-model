package ex.model

import scala.reflect.internal.util.Statistics.Quantity

trait Transaction {
  def timestamp: Timestamp
}

trait SenderRecepientTransaction {
  def sender: Address
  def recipient: Address
}

case class GenesisTransaction private (recipient: Address,
                                       timestamp: Timestamp,
                                       quantity: Long)
    extends Transaction

case class IssueTransaction(sender: Address,
                            name: Array[Byte],
                            description: Array[Byte],
                            issue: AssetVolume,
                            decimals: Byte,
                            reissuable: Boolean,
                            feeMoney: WavesVolume,
                            timestamp: Timestamp)
    extends Transaction

case class ReissueTransaction(sender: Address,
                              issue: AssetVolume,
                              reissuable: Boolean,
                              feeMoney: WavesVolume,
                              timestamp: Timestamp)
    extends Transaction

case class PaymentTransaction(sender: Address,
                              recipient: Address,
                              quantity: WavesVolume,
                              fee: WavesVolume,
                              timestamp: Timestamp)
    extends Transaction

case class TransferTransaction(timestamp: Timestamp,
                               sender: Address,
                               recipient: Address,
                               quantity: Volume,
                               feeMoney: Volume,
                               attachment: Array[Byte])
    extends Transaction

