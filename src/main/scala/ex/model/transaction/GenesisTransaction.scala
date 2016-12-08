package ex.model.transaction

import ex.model._

case class GenesisTransaction private(recipient: Address,
                                      timestamp: Timestamp,
                                      quantity: Long)
  extends Transaction
