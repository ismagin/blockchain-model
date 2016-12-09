package ex

import cats.data.{NonEmptyList, Validated, ValidatedFunctions}
import cats.free.{Free, FreeT}
import cats.{Order => _}
import ex.model.state.Storage._
import cats.data.Validated._
package object model {

  type Height    = Int
  type Timestamp = Long
  type BlockId   = String

  type PublicKey = Array[Byte]
  type Version   = Byte
  type ChainId   = Byte

  type ValidationResult[+A] = Validated[NonEmptyList[String], A]

  type FreeValidationResult[A] = Free[DSL, Validated[NonEmptyList[String], A]]

  def validate(validIf: => Boolean, errorMessage: => String): ValidationResult[Unit] = if (validIf) valid(()) else invalidNel(errorMessage)

}
