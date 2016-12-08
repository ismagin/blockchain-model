package ex.model

object Currency {

  sealed trait Volume
  case class WavesVolume(amount: Long)               extends Volume
  case class AssetVolume(asset: Asset, amount: Long) extends Volume

  type AssetId   = Array[Byte]

  sealed trait Money
  case object Waves              extends Money
  case class Asset(tag: AssetId) extends Money

  type Portfolio = Map[Money, Long]

  def liftVolume(v: Volume): Portfolio = v match {
    case WavesVolume(amt)        => Map(Waves -> amt)
    case AssetVolume(asset, amt) => Map(asset -> amt)
  }
}
