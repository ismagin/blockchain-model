package ex.model.state

import cats.data._
import cats._
import cats.free._
import cats.implicits._
import ex.model._

trait Storage {

  trait DSL[A]
  def lastPaymentTransactionTimestamp(a: Address): DSL[Option[Timestamp]] = ???
  def accBalance(a: Address): DSL[Portfolio]                              = ???
  def lastConfirmedBlockTimestamp(): DSL[Timestamp]                       = ???

  implicit def lift[A](d: DSL[A]): Free[DSL, A] = Free.liftF(d)
  def pure[A](a: A): Free[DSL, A]               = Free.pure(a)

}
