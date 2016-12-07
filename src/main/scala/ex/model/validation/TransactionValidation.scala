package ex.model.validation

import cats.{Applicative, Functor}
import cats.data.{NonEmptyList, Validated, ValidatedFunctions}
import cats.free.{Free, FreeT}
import ex.model._
import ex.model.state.Storage._
import cats.implicits._

object TransactionValidation extends ValidatedFunctions {

  private val MaxTimeForUnconfirmed = 90 * 60 * 1000

  def apply(ruleStartTime: Timestamp)(t: Transaction): FreeValidationResult[Transaction] =
    for {
      time <- lastConfirmedBlockTimestamp()
      r <- if (time > ruleStartTime || t.timestamp - time > MaxTimeForUnconfirmed)
        invalidNel("Transaction creation time more then block's creation time no more then on MaxTimeForUnconfirmed")
      else valid(t)
    } yield r
}
