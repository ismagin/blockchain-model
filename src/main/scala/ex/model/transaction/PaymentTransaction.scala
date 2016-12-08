package ex.model.transaction

import cats.data.ValidatedFunctions
import ex.model._

import cats._, cats.data.Validated, cats.instances.all._
import cats._
import cats.data.Validated
import cats.instances.all._

sealed trait PaymentTransaction extends FromToTransaction {
  def sender: Address
  def recipient: Address
  def quantity: WavesVolume
  def fee: WavesVolume
  def timestamp: Timestamp
}

object PaymentTransaction extends ValidatedFunctions {

  private case class PaymentTransactionImpl(sender: Address, recipient: Address, quantity: WavesVolume, fee: WavesVolume, timestamp: Timestamp)
      extends PaymentTransaction

  def validate(cond: => Boolean, err: => String): ValidationResult[Unit] = if (cond) valid(()) else invalidNel(err)

  def apply(sender: Address,
            recipient: Address,
            quantity: WavesVolume,
            fee: WavesVolume,
            timestamp: Timestamp): ValidationResult[PaymentTransaction] = {
//
//    val x= validate(quantity.amount > 0, "Quantity must be greater than zero") |@|
//      validate(fee.amount > 0, "Fee must be greater than zero") |@|
//      validate(Long.MaxValue - quantity.amount > fee.amount, "Quantity + fee must me less than Long.MaxValue")

    ???

  }
}
