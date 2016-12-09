package ex

import cats.data.{NonEmptyList, Validated}
import cats.data.Validated.{Invalid, Valid}
import ex.model.Timestamp
import ex.model.transaction.Transaction

/**
  * Created by ilya on 09.12.16.
  */
package object testdata {
  case class BananaTranscation(timestamp: Timestamp) extends Transaction

  type ValidationError = Invalid[NonEmptyList[String]]
  type ValidationSuccess = Valid[_]
}
