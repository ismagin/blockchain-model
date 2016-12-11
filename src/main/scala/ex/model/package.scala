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
  type AccValidationResult[+A]         = Validated[ErrorAccumulator, A]
  type FailFastValidationResult[+A]    = Either[Error, A]
  type FreeFailFastValidationResult[A] = Free[DSL, FailFastValidationResult[A]]

  def validate(validIf: => Boolean, errorMessage: => String): AccValidationResult[Unit] = if (validIf) valid(()) else invalidNel(errorMessage)

}
