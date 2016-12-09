package ex.model.transaction

import cats.data.Validated._
import cats.syntax.all._
import ex.model.Currency._
import ex.model._

import scala.util.Try

sealed trait PaymentTransaction extends FromToTransaction {
  def sender: Address
  def recipient: Address
  def quantity: WavesVolume
  def fee: WavesVolume
  def timestamp: Timestamp
}

object PaymentTransaction {

  private case class PaymentTransactionImpl(sender: Address, recipient: Address, quantity: WavesVolume, fee: WavesVolume, timestamp: Timestamp)
      extends PaymentTransaction

  def apply(sender: Address,
            recipient: Address,
            quantity: WavesVolume,
            fee: WavesVolume,
            timestamp: Timestamp): ValidationResult[PaymentTransaction] = {

    val x = validate(quantity.amount > 0, "Quantity must be greater than zero") |@|
        validate(fee.amount > 0, "Fee must be greater than zero") |@|
        validate(sender.chainId == recipient.chainId, "Sener and recipient are from different chains") |@|
        validate(Try { Math.addExact(quantity.amount, fee.amount) }.isSuccess, "Quantity + fee must me less than Long.MaxValue")

    x.map((_, _, _, _) => PaymentTransactionImpl(sender, recipient, quantity, fee, timestamp))

  }
}
