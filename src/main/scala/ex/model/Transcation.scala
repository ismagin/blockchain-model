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
                            issue: AssetMoney,
                            decimals: Byte,
                            reissuable: Boolean,
                            feeMoney: WavesMoney,
                            timestamp: Timestamp)
    extends Transaction

case class ReissueTransaction(sender: Address,
                              issue: AssetMoney,
                              reissuable: Boolean,
                              feeMoney: WavesMoney,
                              timestamp: Timestamp)
    extends Transaction

case class PaymentTransaction(sender: Address,
                              recipient: Address,
                              quantity: WavesMoney,
                              fee: WavesMoney,
                              timestamp: Timestamp)
    extends Transaction
    with SenderRecepientTransaction

case class TransferTransaction(timestamp: Timestamp,
                               sender: Address,
                               recipient: Address,
                               quantity: Money,
                               feeMoney: Money,
                               attachment: Array[Byte])
    extends Transaction
    with SenderRecepientTransaction

