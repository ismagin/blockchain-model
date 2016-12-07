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

  def pure[A](a: A): Free[DSL, A]               = Free.pure(a)

  implicit def liftDSL[A](dsl: DSL[A])(implicit M: Applicative[ValidationResult]): FreeValidationResult[A]               = FreeT.liftF(dsl)
  implicit def liftValidation[A](v: ValidationResult[A])(implicit M: Functor[ValidationResult]): FreeValidationResult[A] = FreeT.liftT(v)

}
