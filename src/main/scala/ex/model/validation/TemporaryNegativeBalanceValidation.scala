package ex.model.validation

import cats.data.NonEmptyList
import cats.data.Validated._
import cats.free.Free
import cats.implicits._
import ex.model.Currency._
import ex.model._
import ex.model.state.Storage
import ex.model.state.Storage.DSL
import ex.model.transaction.FromToTransaction

object TemporaryNegativeBalanceValidation {

  type StateOverrides = Map[Address, Portfolio]

  def apply(tmp: StateOverrides, seq: Seq[FromToTransaction]): FreeValidationResult[StateOverrides] = seq match {
    case h :: tail =>
      (for {
        st   <- validateOne(tmp, h).toEitherT
        rest <- apply(st, tail).toEitherT
      } yield rest).toFree
    case _ => Storage.pure(valid(tmp))
  }

  def effectiveAccountBalance(overrides: StateOverrides, a: Address): Free[DSL, (Portfolio, StateOverrides)] = {
    overrides.get(a) match {
      case Some(p) => Storage.pure((p, overrides))
      case None    => Storage.accBalance(a).map(p => (p, overrides))
    }
  }

  def senderRecipientBalances(overrides: StateOverrides, s: Address, r: Address): Free[DSL, (Portfolio, Portfolio, StateOverrides)] =
    for {
      r1 <- effectiveAccountBalance(overrides, s)
      r2 <- effectiveAccountBalance(overrides, r)
    } yield (r1._1, r2._1, overrides)

  def isPositive(p: Portfolio): Boolean = p.values.forall(_ >= 0)

  def negate(p: Portfolio): Portfolio = p.map { case (k, v) => (k, -v) }

  def validateOne(overrides: StateOverrides, ftt: FromToTransaction): FreeValidationResult[StateOverrides] =
    for {
      r <- senderRecipientBalances(overrides, ftt.sender, ftt.recipient)
      (senderBalance, recipientBalance, updatedState) = r
      totalWithdraw                                   = liftVolume(ftt.quantity) combine liftVolume(ftt.fee)
      newSenderBalance                                = senderBalance combine negate(totalWithdraw)
      newRecipientBalance                             = recipientBalance combine liftVolume(ftt.quantity)
    } yield
      if (isPositive(newSenderBalance)) {
        valid(updatedState ++ Map(ftt.sender -> newSenderBalance, ftt.recipient -> newRecipientBalance))
      } else {
        invalidNel(
          s"Transaction application leads to negative balance of sender(${ftt.sender}):" +
            s" before: $senderBalance, diff: $totalWithdraw. result: $newSenderBalance")
      }

}
