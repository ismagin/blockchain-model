package ex

import cats.data.{EitherT, NonEmptyList => NEL}
import cats.free.Free
import ex.model.state.Storage.DSL
import ex.model.{FreeFailFastValidationResult, _}
import ex.model.transaction.{FromToTransaction, PaymentTransaction, Transaction}
import ex.model.validation.MaxTimeUnconfirmedValidation
//import ex.model.validation.{NegativeBalanceValidation, PreviousPaymentTransactionValidation, MaxTimeUnconfirmedValidation}

object EP extends App with cats.data.ValidatedFunctions {

  val t: PaymentTransaction = ???

  val maxTimeUnconfirmed: (Transaction) => FreeFailFastValidationResult[Transaction] = MaxTimeUnconfirmedValidation(1)
//  val validateBalance: (FromToTransaction) => FreeValidationResult[FromToTransaction]              = NegativeBalanceValidation(123)
//  val validatePaymentTransaction: (PaymentTransaction) => FreeFailFastValidationResult[PaymentTransaction] = PreviousPaymentTransactionValidation(123)

//  val x = validatePaymentTransaction(t).flatMap(validateBalance).flatMap(validate)

//  validate compose validate




  def toEitherT[A](fff: FreeFailFastValidationResult[A]): EitherT[Free[DSL, ?], Error, A] = EitherT(fff)

  implicit class FffOps[A](fff: FreeFailFastValidationResult[A]) {
    def unify = toEitherT(fff)
  }

  def combine(t: Transaction): FreeFailFastValidationResult[Transaction] = {
    val value = for {
      r  <- maxTimeUnconfirmed(t).unify
      r2 <- maxTimeUnconfirmed(r).unify
    } yield r2
    value.value
  }
}
