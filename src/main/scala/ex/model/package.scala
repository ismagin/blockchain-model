package ex

import cats.Monoid
import cats.data.{NonEmptyList, Validated, ValidatedFunctions}
import cats.free.{Free, FreeT}
import cats.{Order => _, _}
import cats.implicits._
import ex.model.state.Storage._
package object model extends ValidatedFunctions {

  type Height    = Int
  type Timestamp = Long
  type BlockId   = String

  type PublicKey = Array[Byte]
  type Version   = Byte
  type ChainId   = Byte

  type ValidationResult[+A] = Validated[NonEmptyList[String], A]

  type FreeValidationResult[A] = FreeT[DSL, Validated[NonEmptyList[String], ?], A]

  def validate(cond: => Boolean, err: => String): ValidationResult[Unit] = if (cond) valid(()) else invalidNel(err)

}
