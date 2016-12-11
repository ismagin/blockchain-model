package ex

import cats.data.{EitherT, Validated, NonEmptyList => NEL}
import cats.free.Free
import ex.model.FreeValidationResult
import ex.model.state.Storage.DSL
import ex.model.transaction.PaymentTransaction
import ex.model.validation.{MaxTimeUnconfirmedValidation, PreviousPaymentTransactionValidation}
import cats.syntax.all._

object EP extends App with cats.data.ValidatedFunctions {

  val t: PaymentTransaction = ???

  val maxTimeUnconfirmed: (PaymentTransaction) => FreeValidationResult[PaymentTransaction]         = MaxTimeUnconfirmedValidation(1)
  val validatePaymentTransaction: (PaymentTransaction) => FreeValidationResult[PaymentTransaction] = PreviousPaymentTransactionValidation(123)

  def freeValidatedToFreeEither[A, B](f: Free[DSL, Validated[B, A]]): Free[DSL, Either[B, A]]        = f.map(_.toEither)
  def freeValidatedToEitherTFree[A, B](fff: Free[DSL, Validated[B, A]]): EitherT[Free[DSL, ?], B, A] = EitherT(freeValidatedToFreeEither(fff))
  def eitherTFreeToFreeValidated[A, B](eit: EitherT[Free[DSL, ?], B, A]): Free[DSL, Validated[B, A]] = eit.value.map(Validated.fromEither)

  implicit class FVOps[A, B](fv: Free[DSL, Validated[B, A]]) {
    def pack: EitherT[Free[DSL, ?], B, A] = freeValidatedToEitherTFree(fv)
  }

  implicit class EiTFOps[A, B](eit: EitherT[Free[DSL, ?], B, A]) {
    def unpack: Free[DSL, Validated[B, A]] = eitherTFreeToFreeValidated(eit)
  }

  def combineMonadically(t: PaymentTransaction): FreeValidationResult[PaymentTransaction] =
    (for {
      s1 <- maxTimeUnconfirmed(t).pack
      s2 <- validatePaymentTransaction(s1).pack
    } yield s2).unpack

  def combineApplicatively(t: PaymentTransaction): FreeValidationResult[PaymentTransaction] =
    for {
      val1 <- maxTimeUnconfirmed(t)
      val2 <- validatePaymentTransaction(t)
    } yield (val1 |@| val2).map((_, _) => t)

}
