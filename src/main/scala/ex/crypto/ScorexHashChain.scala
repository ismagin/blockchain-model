package ex.crypto

import scrypto.hash.{Blake256, Blake2b256, CryptographicHash, Keccak256}

object ScorexHashChain extends CryptographicHash {

  val DigestSize: Int = 32

  def applyHashes(input: Message, hashes: CryptographicHash*): Array[Byte] = {
    require(hashes.nonEmpty)
    require(hashes.forall(_.DigestSize == hashes.head.DigestSize), "Use hash algorithms with the same digest size")
    hashes.foldLeft(input)((bytes, hashFunction) => hashFunction.hash(bytes))
  }

  def hash(data: Array[Byte]): Array[Byte] = applyHashes(data, Blake2b256, Keccak256)
}
