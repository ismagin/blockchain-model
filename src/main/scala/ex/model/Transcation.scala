package ex.model

trait Transaction

case class GenesisTransaction(recipient: Address,
                              timestamp: Timestamp,
                              quantity: Long)

case class IssueTransaction(sender: Account,
                            name: Array[Byte],
                            description: Array[Byte],
                            issue: AssetMoney,
                            decimals: Byte,
                            reissuable: Boolean,
                            feeMoney: WavesMoney,
                            timestamp: Timestamp)

case class ReissueTransaction(sender: Account,
                              issue: AssetMoney,
                              reissuable: Boolean,
                              feeMoney: WavesMoney,
                              timestamp: Timestamp)

case class PaymentTransaction(sender: Account,
                              recipient: Address,
                              quantity: WavesMoney,
                              fee: WavesMoney,
                              timestamp: Timestamp)

case class TransferTransaction(timestamp: Timestamp,
                               sender: Account,
                               recipient: Address,
                               transfer: Money,
                               feeMoney: Money,
                               attachment: Array[Byte])
