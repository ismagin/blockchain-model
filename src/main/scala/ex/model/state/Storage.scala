package ex.model.state

import cats.data._
import cats.free._
import ex.model.Currency.Portfolio
import ex.model._

import scala.language.implicitConversions

object Storage {

  trait DSL[A]

  case class LastConfirmedBlockTimestamp() extends DSL[Timestamp]

  def lastConfirmedBlockTimestamp(): DSL[Timestamp]                       = LastConfirmedBlockTimestamp()
  def lastPaymentTransactionTimestamp(a: Address): DSL[Option[Timestamp]] = ???
  def accBalance(a: Address): DSL[Portfolio]                              = ???

  implicit def lift[A](dsl: DSL[A]): Free[DSL, A]                       = Free.liftF(dsl)
  implicit def lift[A](v: ValidationResult[A]): FreeValidationResult[A] = Free.pure(v)

}
