package ex

import cats.data.{EitherT, NonEmptyList, Validated, ValidatedFunctions}
import cats.free.{Free, FreeT}
import cats.{Order => _}
import ex.model.state.Storage._
import cats.data.Validated._
package object model {

  def freeValidatedToFreeEither[DSL[_], A, B](f: Free[DSL, Validated[B, A]]): Free[DSL, Either[B, A]]        = f.map(_.toEither)
  def freeValidatedToEitherTFree[DSL[_], A, B](f: Free[DSL, Validated[B, A]]): EitherT[Free[DSL, ?], B, A]   = EitherT(freeValidatedToFreeEither(f))
  def eitherTFreeToFreeValidated[DSL[_], A, B](eit: EitherT[Free[DSL, ?], B, A]): Free[DSL, Validated[B, A]] = eit.value.map(Validated.fromEither)

  implicit class FVOps[DSL[_], A, B](fv: Free[DSL, Validated[B, A]]) {
    def toEitherT: EitherT[Free[DSL, ?], B, A] = freeValidatedToEitherTFree(fv)
  }

  implicit class EiTFOps[DSL[_], A, B](eit: EitherT[Free[DSL, ?], B, A]) {
    def toFree: Free[DSL, Validated[B, A]] = eitherTFreeToFreeValidated(eit)
  }

  type Timestamp = Long

  type Error                   = String
  type ErrorAccumulator        = NonEmptyList[Error]
  type ValidationResult[+A]    = Validated[ErrorAccumulator, A]
  type FreeValidationResult[A] = Free[DSL, ValidationResult[A]]

  def validate(validIf: => Boolean, errorMessage: => String): ValidationResult[Unit] = if (validIf) valid(()) else invalidNel(errorMessage)

}
