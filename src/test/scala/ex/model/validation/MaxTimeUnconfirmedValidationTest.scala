package ex.model.validation

import cats.Id
import com.github.nscala_time.time.Imports._
import ex.model.Address
import ex.model.state.Storage
import ex.testdata._
import org.scalatest.{FlatSpec, Matchers}

class MaxTimeUnconfirmedValidationTest extends FlatSpec with Matchers {

  private val lastBlockTime = DateTime.now().getMillis

  implicit val interp = new Storage[Id] {
    def lastConfirmedBlockTimestamp()                   = lastBlockTime
    def previousPaymentTransactionTimestamp(a: Address) = ???
    def accBalance(a: Address)                          = ???
  }

  "MaxTimeUnconfirmedValidation" should "pass if rule is not applicable yet" in {
    val transaction = BananaTranscation(lastBlockTime + 10.minute.millis)
    val validation  = MaxTimeUnconfirmedValidation(lastBlockTime + 5.minutes.millis)(transaction)
    validation shouldBe a[Right[_,_]]
  }

  it should "pass if less than 90 minutes since lastBlockTime" in {
    val transaction = BananaTranscation(lastBlockTime + 85.minutes.millis)
    val validation  = MaxTimeUnconfirmedValidation(0)(transaction)
    validation shouldBe a[Right[_,_]]
  }

  it should "fail if beyond 90 minutes since lastBlockTime" in {
    val transaction = BananaTranscation(lastBlockTime + 91.minutes.millis)
    val validation  = MaxTimeUnconfirmedValidation(0)(transaction)
    validation shouldBe a[Left[_,_]]
  }
}
