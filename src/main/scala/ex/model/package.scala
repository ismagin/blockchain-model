package ex

import cats.data.Validated._
import cats.data.{NonEmptyList, Validated}

package object model {

  type Timestamp = Long

  type Error                = String
  type ErrorAccumulator     = NonEmptyList[Error]
  type ValidationResult[+A] = Validated[ErrorAccumulator, A]
  type Xor[+A]              = Either[Error, A]

  def validate(validIf: => Boolean, errorMessage: => String): ValidationResult[Unit] = if (validIf) valid(()) else invalidNel(errorMessage)

}
