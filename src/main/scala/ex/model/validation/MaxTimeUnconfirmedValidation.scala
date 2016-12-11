package ex.model.validation

import cats.data.EitherT
import cats.data.Validated._
import com.github.nscala_time.time.Imports._
import ex.model.{FreeFailFastValidationResult, _}
import ex.model.state.Storage
import ex.model.transaction.Transaction
import cats.free.{Free, FreeT}
import ex.model.state.Storage.DSL
import cats.instances._

object MaxTimeUnconfirmedValidation {

  private val MaxTimeForUnconfirmed = 90.minutes.millis

  def inv[A](e: Error): Either[Error, A] = Left(e)
  def v[A](a: A): Either[Error, A]       = Right(a)

  def apply(ruleStartTime: Timestamp)(t: Transaction): FreeFailFastValidationResult[Transaction] =
    for {
      time <- Storage.lastConfirmedBlockTimestamp()
      r <- if (time > ruleStartTime && t.timestamp - time > MaxTimeForUnconfirmed)
        inv("Transaction creation time more then block's creation time no more then on MaxTimeForUnconfirmed")
      else v(t)
    } yield r
}
