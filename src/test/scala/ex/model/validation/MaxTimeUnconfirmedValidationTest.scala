package ex.model.validation

import cats.data.{NonEmptyList, Validated}
import cats.{Id, ~>}
import ex.model.Timestamp
import ex.model.state.Storage.{DSL, LastConfirmedBlockTimestamp}
import ex.model.transaction.Transaction
import org.scalatest.{FlatSpec, Matchers}
import cats.data.Validated._
import ex.testdata._
import com.github.nscala_time.time.Imports._
import cats.data.Validated.Invalid

class MaxTimeUnconfirmedValidationTest extends FlatSpec with Matchers {

  private val lastBlockTime = DateTime.now().getMillis

  val interp = new ~>[DSL, Id] {
    override def apply[A](fa: DSL[A]): Id[A] = fa match {
      case LastConfirmedBlockTimestamp() => lastBlockTime
    }
  }

  "MaxTimeUnconfirmedValidation" should "pass if rule is not applicable yet" in {
    val transcation = BananaTranscation(lastBlockTime + 10.minute.millis)
    val validation  = MaxTimeUnconfirmedValidation(lastBlockTime + 5.minutes.millis)(transcation)
    validation.foldMap(interp) shouldBe valid(transcation)
  }

  it should "pass if less than 90 minutes since lastBlockTime" in {
    val transcation = BananaTranscation(lastBlockTime + 85.minutes.millis)
    val validation  = MaxTimeUnconfirmedValidation(0)(transcation)
    validation.foldMap(interp) shouldBe valid(transcation)
  }

  it should "fail if beyond 90 minutes since lastBlockTime" in {
    val transcation = BananaTranscation(lastBlockTime + 91.minutes.millis)
    val validation  = MaxTimeUnconfirmedValidation(0)(transcation)
    validation.foldMap(interp) shouldBe a[ValidationError]
  }
}
