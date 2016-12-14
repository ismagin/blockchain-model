package ex.model.validation

import cats._
import cats.implicits._
import cats.Monad
import cats.data.EitherT
import ex.model.Currency._
import ex.model._
import ex.model.state.Storage
import ex.model.transaction.FromToTransaction

object TemporaryNegativeBalanceValidation {

  type StateOverrides = Map[Address, Portfolio]
  val emptyOverrides = Map.empty[Address, Portfolio]

  def apply[F[_]: Monad: Storage, T <: FromToTransaction](lst: List[T]): F[Xor[StateOverrides]] = {
    lst.foldM(emptyOverrides) { case (ov, t) => EitherT(validateOne[F, T](ov, t)) }.value
  }
  def effectiveAccountBalance[F[_]: Monad: Storage](overrides: StateOverrides, a: Address): F[Portfolio] = {
    overrides.get(a) match {
      case Some(p) => Monad[F].pure(p)
      case None    => Storage.accBalance(a)
    }
  }

  def validateOne[F[_]: Monad: Storage, T <: FromToTransaction](overrides: StateOverrides, ftt: T): F[Xor[StateOverrides]] =
    for {
      senderBalance    <- effectiveAccountBalance[F](overrides, ftt.sender)
      recipientBalance <- effectiveAccountBalance[F](overrides, ftt.recipient)
      totalWithdraw       = liftVolume(ftt.quantity) |+| liftVolume(ftt.fee)
      newSenderBalance    = senderBalance |+| negate(totalWithdraw)
      newRecipientBalance = recipientBalance |+| liftVolume(ftt.quantity)
    } yield
      if (isPositive(newSenderBalance)) {
        Right(overrides ++ Map(ftt.sender -> newSenderBalance, ftt.recipient -> newRecipientBalance))
      } else {
        Left(
          s"Transaction application leads to negative balance of sender(${ftt.sender}):" +
            s" before: $senderBalance, diff: $totalWithdraw. result: $newSenderBalance")
      }

}
