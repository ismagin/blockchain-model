package ex

import cats.data.{EitherT, Validated, NonEmptyList => NEL}
import cats.free.Free
import ex.model.state.Storage.DSL
import ex.model.{FreeValidationResult, _}
import ex.model.transaction.{FromToTransaction, PaymentTransaction, Transaction}
import ex.model.validation.MaxTimeUnconfirmedValidation
//import ex.model.validation.{NegativeBalanceValidation, PreviousPaymentTransactionValidation, MaxTimeUnconfirmedValidation}

object EP extends App with cats.data.ValidatedFunctions {

  val t: PaymentTransaction = ???

  val maxTimeUnconfirmed: (Transaction) => FreeValidationResult[Transaction] = MaxTimeUnconfirmedValidation(1)
  //  val validateBalance: (FromToTransaction) => FreeValidationResult[FromToTransaction]              = NegativeBalanceValidation(123)
  //  val validatePaymentTransaction: (PaymentTransaction) => FreeFailFastValidationResult[PaymentTransaction] = PreviousPaymentTransactionValidation(123)

  //  val x = validatePaymentTransaction(t).flatMap(validateBalance).flatMap(validate)

  //  validate compose validate

  type FreeEither[A] = Free[DSL, Either[ErrorAccumulator, A]]

  def validationResultToEither[A](vr: ValidationResult[A]): Either[ErrorAccumulator, A] = vr.toEither

  def freeValidatioResultToFreeEither[A](f: FreeValidationResult[A]): FreeEither[A] =
    f.map(validationResultToEither)

  def toEitherT[A](fff: FreeEither[A]): EitherT[Free[DSL, ?], ErrorAccumulator, A] = EitherT(fff)

  implicit class FffOps[A](fff: FreeValidationResult[A]) {
    def unify: EitherT[Free[DSL, ?], ErrorAccumulator, A] = {
      val toFreeEither: FreeEither[A] = freeValidatioResultToFreeEither(fff)
      toEitherT(toFreeEither)
    }
  }

  def combine(t: Transaction): FreeValidationResult[Transaction] = {
    val value: EitherT[Free[DSL, ?], ErrorAccumulator, Transaction] = for {
      r <- maxTimeUnconfirmed(t).unify
      r2 <- maxTimeUnconfirmed(r).unify
    } yield r2
    value.value.map(Validated.fromEither)
  }
}
