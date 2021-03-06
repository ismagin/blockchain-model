package ex.model

import ex.model.transaction.Transaction
import Block._

trait Block
case class OneBlock(id: BlockId, prev: BlockId, height: Height, timestamp: Timestamp, generator: Address, txns: Seq[Transaction])
case class GenesisBlock(timestamp: Timestamp, txns: Seq[Transaction])

object Block {
  type Height  = Int
  type BlockId = String
}
