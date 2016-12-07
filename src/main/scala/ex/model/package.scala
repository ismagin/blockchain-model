package ex

import cats.Monoid
import cats.data.{NonEmptyList, Validated}
import cats.{Order => _, _}
import cats.implicits._
package object model {

  type Height    = Int
  type Timestamp = Long
  type BlockId   = String
  type AssetId   = Array[Byte]

  sealed trait Volume
  case class WavesVolume(amount: Long)               extends Volume
  case class AssetVolume(asset: Asset, amount: Long) extends Volume

  sealed trait Money
  case object Waves              extends Money
  case class Asset(tag: AssetId) extends Money

  type Portfolio = Map[Money, Long]

  type PublicKey = Array[Byte]
  type Version   = Byte
  type ChainId   = Byte

  type ValidationResult[A] = Validated[NonEmptyList[String], A]

  def liftVolume(v: Volume): Portfolio = v match {
    case WavesVolume(amt)        => Map(Waves -> amt)
    case AssetVolume(asset, amt) => Map(asset -> amt)
  }

}
