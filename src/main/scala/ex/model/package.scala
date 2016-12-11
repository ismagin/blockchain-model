package ex

import cats.data.{EitherT, NonEmptyList, Validated, ValidatedFunctions}
import cats.free.{Free, FreeT}
import cats.{Order => _}
import ex.model.state.Storage._
import cats.data.Validated._
package object model {

  type Timestamp = Long

  type Error                           = String
  type ErrorAccumulator                = NonEmptyList[Error]
  type ValidationResult[+A]         = Validated[ErrorAccumulator, A]
  type FreeValidationResult[A] = Free[DSL, ValidationResult[A]]

  def validate(validIf: => Boolean, errorMessage: => String): ValidationResult[Unit] = if (validIf) valid(()) else invalidNel(errorMessage)

}
