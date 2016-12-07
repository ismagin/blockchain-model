package ex

import cats._
import cats.data.Validated
import cats.free.Free
import cats.implicits._
import cats.data.{NonEmptyList => NEL}
import cats.data.ValidatedFunctions
import cats.instances.all._
import ex.model._
import ex.model.validation.{NegativeBalanceValidation, PaymentTransactionValidation, TransactionValidation}

object EP extends App with cats.data.ValidatedFunctions {

  val a: Address = ???

  def validate                                          = TransactionValidation(1) _
  def validateBalance(ftt: FromToTransaction)           = NegativeBalanceValidation(123) _
  def validatePaymentTransaction(p: PaymentTransaction) = PaymentTransactionValidation(a, 123) _



}
