package ex.model.transaction

import ex.model.Currency._
import ex.model._

trait Transaction {
  def timestamp: Timestamp
}

trait FromToTransaction extends Transaction {
  def sender: Address
  def recipient: Address
  def fee: Volume
  def quantity: Volume
}
