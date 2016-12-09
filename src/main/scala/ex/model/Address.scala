package ex.model

import cats.data.Validated._
import ex.crypto.ScorexHashChain

sealed trait Address {

  def publicKey: PublicKey
  def version: Version
  def chainId: ChainId
}

object Address {

  private case class AddressImpl(version: Version, chainId: ChainId, publicKey: PublicKey) extends Address

  val ChecksumLength = 4
  val PkLength       = 20

  def apply(bytes: Array[Byte]): ValidationResult[Address] = {
    if (bytes.length != 26) {
      invalidNel(s"Invalid length: expected: 26 actual: ${bytes.length}")
    } else {
      val checksum     = bytes.takeRight(ChecksumLength)
      val dropChecksum = bytes.dropRight(ChecksumLength)
      if (ScorexHashChain.hash(dropChecksum).take(ChecksumLength) sameElements checksum) {
        val ver     = bytes(0)
        val chainId = bytes(1)
        val pkHash  = bytes.slice(2, 2 + PkLength)
        valid(AddressImpl(ver, chainId, pkHash))
      } else {
        invalidNel(s"Invalid checksum: expected: ${ScorexHashChain.hash(bytes)}")
      }
    }
  }
}
