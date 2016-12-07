package ex.model.validation

import cats.data.{NonEmptyList, Validated, ValidatedFunctions}
import cats.free.Free
import ex.model._
import ex.model.state.Storage
import cats.implicits._

object TransactionValidation extends ValidatedFunctions with Storage {

  private val MaxTimeForUnconfirmed = 90 * 60 * 1000

  def apply(ruleStartTime: Timestamp)(t: Transaction) =
    for {
      time <- lastConfirmedBlockTimestamp()
    } yield
      if (time > ruleStartTime || t.timestamp - time > MaxTimeForUnconfirmed)
        invalidNel("Transaction creation time more then block's creation time no more then on MaxTimeForUnconfirmed")
      else valid(t)

}
