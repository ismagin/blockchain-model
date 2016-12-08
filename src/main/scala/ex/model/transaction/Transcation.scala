package ex.model.transaction

import ex.model._
import ex.model.Currency._

trait Transaction {
  def timestamp: Timestamp
}

trait FromToTransaction extends Transaction {
  def sender: Address
  def recipient: Address
  def fee: Volume
  def quantity: Volume
}
