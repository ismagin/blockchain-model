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

  case class WavesMoney(amount: Long)
  case class AssetMoney(amount: Long, tag: AssetId)
  type Money = Either[WavesMoney, AssetMoney]

  case class Portfolio(waves: Long, assets: Map[AssetId, Long])

  type PublicKey = Array[Byte]
  type Version   = Byte
  type ChainId   = Byte

  type ValidationResult[A] = Validated[NonEmptyList[String], A]

  implicit def liftWaves(w: WavesMoney): Money = Left(w)
  implicit def liftAsset(a: AssetMoney): Money = Right(a)
  def liftMoney(m: Money): Portfolio = m match {
    case Left(w)  => Portfolio(w.amount, Map.empty)
    case Right(a) => Portfolio(0, Map(a.tag -> a.amount))
  }

  implicit val portfolioMonoid = new Monoid[Portfolio] {
    override def empty = Portfolio(0,Map.empty)
    override def combine(x: Portfolio, y: Portfolio) = Portfolio(x.waves+y.waves,x.assets.combine(y.assets))
  }
}
