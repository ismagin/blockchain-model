package ex

import cats.syntax.all._
import ex.model.FreeValidationResult
import ex.model.transaction.PaymentTransaction
import ex.model.validation.{MaxTimeUnconfirmedValidation, PreviousPaymentTransactionValidation}

object EP extends App with cats.data.ValidatedFunctions {

  val maxTimeUnconfirmed: (PaymentTransaction) => FreeValidationResult[PaymentTransaction]         = MaxTimeUnconfirmedValidation(1)
  val validatePaymentTransaction: (PaymentTransaction) => FreeValidationResult[PaymentTransaction] = PreviousPaymentTransactionValidation(123)

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
