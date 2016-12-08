package ex.model

import cats.data.ValidatedFunctions
import ex.crypto.ScorexHashChain

sealed trait Address {

  def publicKey: PublicKey
  def version: Version
  def chainId: ChainId
}

object Address extends ValidatedFunctions {

  private case class AddressImpl(version: Version, chainId: ChainId, publicKey: PublicKey) extends Address

  def apply(bytes: Array[Byte]): ValidationResult[Address] = {
    if (bytes.length != 26) {
      invalidNel(s"Invalid length: ${bytes.length}. Required length is 26")
    } else {
      val ver      = bytes(0)
      val chainId  = bytes(1)
      val pk       = bytes.slice(2, 23)
      val checksum = bytes.takeRight(4)
      if (ScorexHashChain.hash(bytes) sameElements checksum) {
        valid(AddressImpl(ver, chainId, pk))
      } else {
        invalidNel("Invalid checksum!")
      }
    }
  }
}
