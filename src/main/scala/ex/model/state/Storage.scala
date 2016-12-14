package ex.model.state

import cats.Monad
import cats.data._
import cats.free._
import ex.model.Currency.Portfolio
import ex.model._

import scala.language.implicitConversions

trait Storage[F[_]] {

  def lastConfirmedBlockTimestamp(): F[Timestamp]
  def previousPaymentTransactionTimestamp(a: Address): F[Option[Timestamp]]
  def accBalance(a: Address): F[Portfolio]
}

object Storage {
  def lastConfirmedBlockTimestamp[F[_]]()(implicit F: Storage[F]): F[Timestamp]                           = F.lastConfirmedBlockTimestamp()
  def previousPaymentTransactionTimestamp[F[_]](a: Address)(implicit F: Storage[F]): F[Option[Timestamp]] = F.previousPaymentTransactionTimestamp(a)
  def accBalance[F[_]](a: Address)(implicit F: Storage[F]): F[Portfolio]                                  = F.accBalance(a)

}
