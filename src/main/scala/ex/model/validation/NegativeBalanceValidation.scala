package ex.model.validation

import cats.data.{NonEmptyList, StateT, Validated}
import cats.data.Validated._
import cats.free.Free
import cats.free.Free.{FlatMapped, Pure, Suspend}
import cats.implicits._
import ex.model.Currency._
import ex.model._
import ex.model.transaction.FromToTransaction
import cats.{Id, ~>}
import ex.model.state.Storage
import ex.model.state.Storage.DSL

object NegativeBalanceValidation {

  type TmpAccountStates = Map[Address, Portfolio]

  def apply(tmp: TmpAccountStates, seq: Seq[FromToTransaction]): FreeValidationResult[TmpAccountStates] = seq match {
    case h :: tail =>
      validateOne(tmp, h).map {
        case Valid(state) => apply(state, tail)
        case Invalid(e)   => Storage.pure(invalid[NonEmptyList[String], TmpAccountStates](e))
      }.flatten
    case _ => Storage.pure(valid(tmp))
  }

  def effectiveAccountBalance(temporaryState: TmpAccountStates, a: Address): Free[DSL, (Portfolio, TmpAccountStates)] = {
    temporaryState.get(a) match {
      case Some(p) => Storage.pure((p, temporaryState))
      case None    => Storage.accBalance(a).map(p => (p, temporaryState + (a -> p)))
    }
  }

  def senderRecipientBalances(temporaryState: TmpAccountStates, s: Address, r: Address): Free[DSL, (Portfolio, Portfolio, TmpAccountStates)] =
    for {
      r1 <- effectiveAccountBalance(temporaryState, s)
      r2 <- effectiveAccountBalance(r1._2, r)
    } yield (r1._1, r2._1, r2._2)

  def isPositive(p: Portfolio): Boolean = p.values.forall(_ >= 0)

  def negate(p: Portfolio): Portfolio = p.map { case (k, v) => (k, -v) }

  def validateOne(temporaryState: TmpAccountStates, ftt: FromToTransaction): FreeValidationResult[TmpAccountStates] =
    for {
      r <- senderRecipientBalances(temporaryState, ftt.sender, ftt.recipient)
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
