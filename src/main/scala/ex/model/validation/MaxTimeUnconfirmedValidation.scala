package ex.model.validation

import cats.data.Validated._
import com.github.nscala_time.time.Imports._
import ex.model.state.Storage
import ex.model.transaction.Transaction
import ex.model.{FreeValidationResult, _}

object MaxTimeUnconfirmedValidation {

  private val MaxTimeForUnconfirmed = 90.minutes.millis

  def apply(ruleStartTime: Timestamp)(t: Transaction): FreeValidationResult[Transaction] =
    for {
      time <- Storage.lastConfirmedBlockTimestamp()
      r <- if (time > ruleStartTime && t.timestamp - time > MaxTimeForUnconfirmed)
        invalidNel("Transaction creation time more then block's creation time no more then on MaxTimeForUnconfirmed")
      else valid(t)
    } yield r
}
