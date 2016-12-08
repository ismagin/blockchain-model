package ex.model

import ex.model.transaction.Transaction

trait Block
case class OneBlock(id: BlockId, prev: BlockId, height: Height, timestamp: Timestamp, generator: Address, txns: Seq[Transaction])
case class GenesisBlock(timestamp: Timestamp, txns: Seq[Transaction])
