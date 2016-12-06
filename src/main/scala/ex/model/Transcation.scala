package ex.model

trait Transaction

case class GenesisTransaction(recipient: Address,
                              timestamp: Long,
                              quantity: Long)

case class IssueTransaction(sender: Account,
                            name: Array[Byte],
                            description: Array[Byte],
                            issue: AssetMoney,
                            decimals: Byte,
                            reissuable: Boolean,
                            feeMoney: WavesMoney,
                            timestamp: Long)

case class ReissueTransaction(sender: Account,
                              issue: AssetMoney,
                              reissuable: Boolean,
                              feeMoney: WavesMoney,
                              timestamp: Long)

case class PaymentTransaction(sender: Account,
                              recipient: Address,
                              quantity: Long,
                              fee: Long,
                              timestamp: Long)

case class TransferTransaction(timestamp: Long,
                               sender: Account,
                               recipient: Address,
                               transfer: Money,
                               feeMoney: Money,
                               attachment: Array[Byte])
