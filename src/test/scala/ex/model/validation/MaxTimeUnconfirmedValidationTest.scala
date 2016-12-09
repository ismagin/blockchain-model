package ex.model.validation

import cats.{Id, ~>}
import com.github.nscala_time.time.Imports._
import ex.model.state.Storage.{DSL, LastConfirmedBlockTimestamp}
import ex.testdata._
import org.scalatest.{FlatSpec, Matchers}

class MaxTimeUnconfirmedValidationTest extends FlatSpec with Matchers {

  private val lastBlockTime = DateTime.now().getMillis

  val interp = new ~>[DSL, Id] {
    override def apply[A](fa: DSL[A]): Id[A] = fa match {
      case LastConfirmedBlockTimestamp() => lastBlockTime
    }
  }

  "MaxTimeUnconfirmedValidation" should "pass if rule is not applicable yet" in {
    val transaction = BananaTranscation(lastBlockTime + 10.minute.millis)
    val validation  = MaxTimeUnconfirmedValidation(lastBlockTime + 5.minutes.millis)(transaction)
    validation.foldMap(interp) shouldBe a[ValidationSuccess]
  }

  it should "pass if less than 90 minutes since lastBlockTime" in {
    val transaction = BananaTranscation(lastBlockTime + 85.minutes.millis)
    val validation  = MaxTimeUnconfirmedValidation(0)(transaction)
    validation.foldMap(interp) shouldBe a[ValidationSuccess]
  }

  it should "fail if beyond 90 minutes since lastBlockTime" in {
    val transaction = BananaTranscation(lastBlockTime + 91.minutes.millis)
    val validation  = MaxTimeUnconfirmedValidation(0)(transaction)
    validation.foldMap(interp) shouldBe a[ValidationError]
  }
}
