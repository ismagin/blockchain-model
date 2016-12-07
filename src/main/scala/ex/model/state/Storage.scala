package ex.model.state

import cats.data._
import cats._
import cats.free._
import cats.implicits._
import ex.model._

trait Storage {

  trait DSL[A]
  def lastPaymentTransactionTimestamp(a: Account): DSL[Option[Timestamp]] = ???
  def accBalance(a: Account): DSL[Portfolio]                              = ???
  def timeNow(): DSL[Timestamp]                                           = ???

  implicit def lift[A](d: DSL[A]): Free[DSL, A] = Free.liftF(d)
  def pure[A](a: A): Free[DSL, A]     = Free.pure(a)

//
//  val envBasedFunc: Reader[(Timestamp, Environment), Timestamp] = Reader {
//    case (time: Timestamp, env: Environment) => {
//      time + env.chainId
//    }
//  }
//
//  val dslBasedFunc: Free[DSL, Long] = for {
//    maybets: Option[Timestamp] <- LastValidPaymentTransactionTimestamp(Account(Array.empty[Byte]))
//    ls = maybets.getOrElse(123L)
//  } yield ls
//
//  def readerOfTimeAndDsl(a: Account) = Reader { case(time: Timestamp) => for {
//
//  }
//
//  }
}
