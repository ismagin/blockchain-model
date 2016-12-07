package ex.model

trait Transaction

case class GenesisTransaction private (recipient: Address,
                              timestamp: Timestamp,
                              quantity: Long)

case class IssueTransaction(sender: Address,
                            name: Array[Byte],
                            description: Array[Byte],
                            issue: AssetMoney,
                            decimals: Byte,
                            reissuable: Boolean,
                            feeMoney: WavesMoney,
                            timestamp: Timestamp)

case class ReissueTransaction(sender: Address,
                              issue: AssetMoney,
                              reissuable: Boolean,
                              feeMoney: WavesMoney,
                              timestamp: Timestamp)

case class PaymentTransaction(sender: Address,
                              recipient: Address,
                              quantity: WavesMoney,
                              fee: WavesMoney,
                              timestamp: Timestamp)

case class TransferTransaction(timestamp: Timestamp,
                               sender: Address,
                               recipient: Address,
                               transfer: Money,
                               feeMoney: Money,
                               attachment: Array[Byte])

