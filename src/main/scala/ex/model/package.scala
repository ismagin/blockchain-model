package ex

import cats.data.{NonEmptyList, Validated}

package object model {

  type Height    = Int
  type Timestamp = Long
  type BlockId   = String
  type AssetId   = Array[Byte]

  case class WavesMoney(amount: Long)
  case class AssetMoney(amount: Long, tag: AssetId)
  type Money = Either[WavesMoney, AssetMoney]

  case class Portfolio(wavesMoney: WavesMoney, assetMoney: Seq[AssetMoney])

  type PublicKey = Array[Byte]
  type Version   = Byte
  type ChainId   = Byte

  type ValidationResult[A] = Validated[NonEmptyList[String], A]
}
