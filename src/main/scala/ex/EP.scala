package ex

import cats._
import cats.data.Validated
import cats.free.Free
import cats.implicits._
import cats.data.{NonEmptyList => NEL}
import cats.data.ValidatedFunctions
import cats.instances.all._

object EP extends App with cats.data.ValidatedFunctions {

  object State {
    trait DSL[A]
    case class IsValid(id: BlockId)
  }

  type Height     = Int
  type Timestamp  = Long
  type BlockId    = String
  type PrivateKey = Array[Byte]
  type PublicKey  = Array[Byte]
  type Address    = Array[Byte]

  private val ChecksumLength = 4
  private val HashLength     = 20
  val AddressVersion: Byte   = 1
  val AddressLength          = 1 + 1 + ChecksumLength + HashLength

  def addressByPublicKey(publicKey: PublicKey): Address = {
//    val publicKeyHash   = wbc.secureHash.hash(publicKey).take(HashLength)
//    val withoutChecksum = AddressVersion +: wbc.configuration.chainId +: publicKeyHash
//    withoutChecksum ++ calcCheckSum(withoutChecksum)
    ???
  }

  case class Account(publicKey: PublicKey, privateKey: Option[PrivateKey] = None)

  trait Transaction

  trait Block
  case class OneBlock(id: BlockId, prev: BlockId, height: Height, timestamp: Timestamp, generator: Account, txns: Seq[Transaction])
  case class GenesisBlock(timestamp: Timestamp, txns: Seq[Transaction])

  type ValidationResult[A] = Validated[NEL[String], A]

  def validateBlock(block: Block): Free[State.DSL, ValidationResult[Block]] = ???

  trait BlockValidator[B <: Block] {
    def validate(b: B): ValidationResult[B]
  }

  def calcCheckSum(data: Array[Byte]): Array[Byte] =
    ScorexHashChain.hash(data).take(ChecksumLength)

  def validateAccount(chainId: Byte, account: Account): ValidationResult[Account] = {
    val addressBytes = addressByPublicKey(account.publicKey)
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
        invalid(NEL.of(s"$checkSumGenerated != $checkSum"))
      } else {
        valid(account)
      }
    }

  }

}
