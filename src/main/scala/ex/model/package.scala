package ex

import cats.data.{NonEmptyList, Validated}

/**
  * Created by ilya on 06.12.16.
  */
package object model {

  type Height     = Int
  type Timestamp  = Long
  type BlockId    = String
  type PrivateKey = Array[Byte]
  type PublicKey  = Array[Byte]
  type Address    = Array[Byte]

  type ValidationResult[A] = Validated[NonEmptyList[String], A]
}
