package ex

import cats.data.{NonEmptyList => NEL}
import ex.model._
import ex.model.transaction.{FromToTransaction, PaymentTransaction, Transaction}
import ex.model.validation.{NegativeBalanceValidation, PaymentTransactionValidation, TransactionValidation}

object EP extends App with cats.data.ValidatedFunctions {

  val a: Address            = ???
  val t: PaymentTransaction = ???

  val validate: (Transaction) => FreeValidationResult[Transaction]                                 = TransactionValidation(1)
  val validateBalance: (FromToTransaction) => FreeValidationResult[FromToTransaction]              = NegativeBalanceValidation(123)
  val validatePaymentTransaction: (PaymentTransaction) => FreeValidationResult[PaymentTransaction] = PaymentTransactionValidation(a, 123)

//  val x = validatePaymentTransaction(t).flatMap(validateBalance).flatMap(validate)

//  validate compose validate
}
