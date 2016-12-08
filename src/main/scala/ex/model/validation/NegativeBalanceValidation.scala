package ex.model.validation

import cats.data.ValidatedFunctions
import cats.implicits._
import ex.model.Currency._
import ex.model._
import ex.model.state.Storage._
import ex.model.transaction.FromToTransaction
object NegativeBalanceValidation extends ValidatedFunctions {

  def apply(startTime: Timestamp)(t: FromToTransaction): FreeValidationResult[FromToTransaction] =
    for {
      time <- lastConfirmedBlockTimestamp()
      r <- if (time >= startTime)
        lift(valid(t))
      else {
        for {
          senderBalance <- accBalance(t.sender)
          res <- if (senderBalanceStaysPositive(senderBalance, t.quantity, t.fee)) valid(t)
          else invalidNel("Insufficient Sender Funds")
        } yield res
      }
    } yield r

  private def senderBalanceStaysPositive(senderBalance: Portfolio, quantity: Volume, fee: Volume): Boolean = {
    val totalTransfer = liftVolume(quantity) combine liftVolume(fee)
    totalTransfer.forall {
      case (m: Money, amt: Long) =>
        senderBalance.get(m) match {
          case Some(v) if v >= amt => true
          case _                   => false
        }
    }
  }
}
