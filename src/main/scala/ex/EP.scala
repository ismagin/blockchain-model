package ex

import cats._
import cats.data.Validated
import cats.free.Free
import cats.implicits._
import cats.data.{NonEmptyList => NEL}
import cats.data.ValidatedFunctions
import cats.instances.all._

import ex.model._

object EP extends App with cats.data.ValidatedFunctions {
  object State {
    trait DSL[A]
    case class IsValid(id: BlockId)
  }

  def validateBlock(block: Block): Free[State.DSL, ValidationResult[Block]] = ???

  trait BlockValidator[B <: Block] {
    def validate(b: B): ValidationResult[B]
  }

}
