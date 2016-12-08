package ex.model.state

import cats.data._
import cats._
import cats.free._
import cats.implicits._
import ex.model._
import scala.language.implicitConversions

object Storage {

  trait DSL[A]
  def lastPaymentTransactionTimestamp(a: Address): DSL[Option[Timestamp]] = ???
  def accBalance(a: Address): DSL[Portfolio]                              = ???
  def lastConfirmedBlockTimestamp(): DSL[Timestamp]                       = ???

//  def pure[A](a: A): Free[DSL, A] = Free.pure(a)

  implicit def lift[A](dsl: DSL[A]): FreeValidationResult[A]                   = FreeT.liftF(dsl)
  implicit def lift[A](v: ValidationResult[A]): FreeValidationResult[A] = FreeT.liftT(v)

  def pure[A](a: A): FreeValidationResult[A] = lift(Validated.valid(a))
}
