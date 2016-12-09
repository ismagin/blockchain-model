package ex.model

import cats.data.NonEmptyList
import cats.data.Validated.{Invalid, Valid}
import ex.model.Currency.WavesVolume
import ex.model.transaction.PaymentTransaction
import ex.testdata._
import org.scalatest.{FlatSpec, FunSuite, Matchers}
import scrypto.encode.Base58

class AddressTest extends FlatSpec with Matchers {

  "Address" should "be reconstructed from valid Base58 string" in {
    Address(Base58.decode("3P31zvGdh6ai6JK6zZ18TjYzJsa1B83YPoj").get) shouldBe a[ValidationSuccess]
  }
  it should "not be reconstructed if hash is invalid" in {
    Address(Base58.decode("3P31zvGdh6ai6JK6zZ18TjYzJsa1B83YPo3").get) shouldBe a[ValidationError]
  }
}