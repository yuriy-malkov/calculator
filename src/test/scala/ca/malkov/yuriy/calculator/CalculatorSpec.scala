package ca.malkov.yuriy.calculator

import akka.actor.testkit.typed.scaladsl.{ActorTestKit, TestProbe}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CalculatorSpec
  extends AnyWordSpec
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Matchers {

  val testKit: ActorTestKit = ActorTestKit()
  val probe: TestProbe[CalculatorSpec.Total] = testKit.createTestProbe[CalculatorSpec.Total]()
  val calculator: ActorRef[CalculatorSpec.Command] = testKit.spawn(CalculatorSpec(0), "calculator")

  override def afterAll(): Unit = testKit.shutdownTestKit()

  override def afterEach(): Unit = {
    calculator ! CalculatorSpec.Reset()
  }

  "Calculator" must {
    val currentValue = 0
    "addition: 0 + 1 = 1" in {
      calculator ! CalculatorSpec.Add(1)
      calculator ! CalculatorSpec.Result(probe.ref)
      probe.expectMessage(CalculatorSpec.Total(1))
    }

    "subtraction: 5 - 3 = 2" in {
      calculator ! CalculatorSpec.Add(5)
      calculator ! CalculatorSpec.Subtract(3)
      calculator ! CalculatorSpec.Result(probe.ref)
      probe.expectMessage(CalculatorSpec.Total(2))
    }

    "reset: 10 to 0" in {
      calculator ! CalculatorSpec.Add(10)
      calculator ! CalculatorSpec.Reset()
      calculator ! CalculatorSpec.Result(probe.ref)
      probe.expectMessage(CalculatorSpec.Total(0))
    }

    "multiplication: 2 * 3 = 6" in {
      calculator ! CalculatorSpec.Add(2)
      calculator ! CalculatorSpec.Multiply(3)
      calculator ! CalculatorSpec.Result(probe.ref)
      probe.expectMessage(CalculatorSpec.Total(6))
    }

    "division: 9 / 3 = 3" in {
      calculator ! CalculatorSpec.Add(9)
      calculator ! CalculatorSpec.Divide(3)
      calculator ! CalculatorSpec.Result(probe.ref)
      probe.expectMessage(CalculatorSpec.Total(3))
    }
  }
}

object CalculatorSpec {

  sealed trait Command
  final case class Add(term: Int) extends Command
  final case class Subtract(term: Int) extends Command
  final case class Multiply(term: Int) extends Command
  final case class Divide(term: Int) extends Command
  final case class Reset() extends Command
  final case class Result(replyTo: ActorRef[Total]) extends Command
  final case class Total(n: Int)

  // create initial state of calculator
  def apply(value: Int): Behavior[Command] = calculator(value)

  private def calculator(total: Int): Behavior[Command] =
    Behaviors.receive((context, message) => {
      message match {
        case Add(term) =>
          calculator(total + term)
        case Subtract(term) =>
          calculator(total - term)
        case Multiply(term) =>
          calculator(total * term)
        case Divide(term) =>
          calculator(total / term)
        case Reset() =>
          calculator(0)
        case Result(replyTo) =>
          replyTo ! Total(total)
          Behaviors.same
      }
    })
}