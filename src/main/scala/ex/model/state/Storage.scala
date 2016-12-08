package ex.model.state

import cats.data._
import cats.free._
import ex.model.Currency.Portfolio
import ex.model._

import scala.language.implicitConversions

object Storage {

  trait DSL[A]
  def lastPaymentTransactionTimestamp(a: Address): DSL[Option[Timestamp]] = ???
  def accBalance(a: Address): DSL[Portfolio]                              = ???
  def lastConfirmedBlockTimestamp(): DSL[Timestamp]                       = ???

  implicit def lift[A](dsl: DSL[A]): FreeValidationResult[A]            = FreeT.liftF(dsl)
  implicit def lift[A](v: ValidationResult[A]): FreeValidationResult[A] = FreeT.liftT(v)

  def pure[A](a: A): FreeValidationResult[A] = lift(Validated.valid(a))
}
