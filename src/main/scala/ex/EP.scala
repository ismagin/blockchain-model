package ex

import cats.data.{NonEmptyList => NEL}
import ex.model._
import ex.model.transaction.{FromToTransaction, PaymentTransaction, Transaction}
import ex.model.validation.{NegativeBalanceValidation, PreviousPaymentTransactionValidation, MaxTimeUnconfirmedValidation}

object EP extends App with cats.data.ValidatedFunctions {

  val t: PaymentTransaction = ???

  val maxTimeUnconfirmed: (Transaction) => FreeValidationResult[Transaction]                                 = MaxTimeUnconfirmedValidation(1)
  val validateBalance: (FromToTransaction) => FreeValidationResult[FromToTransaction]              = NegativeBalanceValidation(123)
  val validatePaymentTransaction: (PaymentTransaction) => FreeValidationResult[PaymentTransaction] = PreviousPaymentTransactionValidation(123)

//  val x = validatePaymentTransaction(t).flatMap(validateBalance).flatMap(validate)

//  validate compose validate
}
