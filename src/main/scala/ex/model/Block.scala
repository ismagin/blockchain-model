package ex.model

trait Block
case class OneBlock(id: BlockId, prev: BlockId, height: Height, timestamp: Timestamp, generator: Account, txns: Seq[Transaction])
case class GenesisBlock(timestamp: Timestamp, txns: Seq[Transaction])
