package ex.model

import cats.data.ValidatedFunctions
import ex.ScorexHashChain

case class Account(publicKey: PublicKey, privateKey: Option[PrivateKey] = None)

object Account extends ValidatedFunctions {

  private val ChecksumLength       = 4
  private val HashLength           = 20
  private val AddressVersion: Byte = 1
  private val AddressLength        = 1 + 1 + ChecksumLength + HashLength

  def addressByPublicKey(chainId: Byte, publicKey: PublicKey): Address = {
    val publicKeyHash   = ScorexHashChain.hash(publicKey).take(HashLength)
    val withoutChecksum = AddressVersion +: chainId +: publicKeyHash
    withoutChecksum ++ calcCheckSum(withoutChecksum)
  }

  def calcCheckSum(data: Array[Byte]): Array[Byte] =
    ScorexHashChain.hash(data).take(ChecksumLength)

  def validateAccount(chainId: Byte, account: Account): ValidationResult[Account] = {
    val addressBytes = addressByPublicKey(chainId, account.publicKey)
    val version      = addressBytes.head
    val network      = addressBytes.tail.head
    if (version != AddressVersion) {
      invalidNel(s"$version != $AddressVersion")
    } else if (network != chainId) {
      invalidNel(s"$network != $chainId")
    } else if (addressBytes.length != AddressLength) {
      invalidNel(s"${addressBytes.length} != $AddressLength")
    } else {
      val checkSum          = addressBytes.takeRight(ChecksumLength)
      val checkSumGenerated = calcCheckSum(addressBytes.dropRight(ChecksumLength))
      if (!checkSum.sameElements(checkSumGenerated)) {
        invalidNel(s"$checkSumGenerated != $checkSum")
      } else {
        valid(account)
      }
    }

  }

}
