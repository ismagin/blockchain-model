package ex.model.validation

import cats.{Id, ~>}
import ex.model.Timestamp
import ex.model.state.Storage.{DSL, LastConfirmedBlockTimestamp}
import ex.model.transaction.Transaction
import org.scalatest.{FlatSpec, Matchers}
import cats.data.Validated._

class MaxTimeUnconfirmedValidationTest extends FlatSpec with Matchers {

  "MaxTimeUnconfirmedValidation" should "pass if not yet applicable" in {

    val bananaTranscaction = new Transaction {
      override def timestamp: Timestamp = 124
    }

    val validation = MaxTimeUnconfirmedValidation(123)(bananaTranscaction)

    val interp = new ~>[DSL, Id] {
      override def apply[A](fa: DSL[A]): Id[A] = fa match {
        case LastConfirmedBlockTimestamp() => 123
      }
    }

    validation.foldMap(interp) shouldBe valid(bananaTranscaction)

  }
}
