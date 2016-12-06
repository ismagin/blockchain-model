package ex

import cats.data.{NonEmptyList, Validated}

package object model {

  type Height    = Int
  type Timestamp = Long
  type BlockId   = String

  type AssetId    = Array[Byte]
  type PrivateKey = Array[Byte]
  type PublicKey  = Array[Byte]
  type Address    = Array[Byte]

  type ValidationResult[A] = Validated[NonEmptyList[String], A]

  case class WavesMoney(amount: Long)
  case class AssetMoney(amount: Long, tag: AssetId)

  type Money = Either[WavesMoney, AssetMoney]
}
