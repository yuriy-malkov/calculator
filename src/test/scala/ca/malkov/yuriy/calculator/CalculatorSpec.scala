package ca.malkov.yuriy.calculator

import akka.actor.testkit.typed.scaladsl.ActorTestKit
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class CalculatorSpec
  extends AnyWordSpec
    with BeforeAndAfterAll
    with Matchers {

  val testKit: ActorTestKit = ActorTestKit()

  override def afterAll(): Unit = testKit.shutdownTestKit()

  "Calculator" must {
    val initialValue = 0
    // create test probe (test actor) that expects a Total
    val probe = testKit.createTestProbe[CalculatorSpec.Total]()
    // Spawn calculator actor with initial value
    val calculator = testKit.spawn(CalculatorSpec(initialValue), "calculator")
    "add term to initial value" in {
      val term = 1
      val expectedValue = 1
      calculator ! CalculatorSpec.Add(term)
      calculator ! CalculatorSpec.Result(probe.ref)
      probe.expectMessage(CalculatorSpec.Total(expectedValue))
    }

    "subtract term from current value" in {
      val term = 3
      val expectedValue = -2
      calculator ! CalculatorSpec.Subtract(term)
      calculator ! CalculatorSpec.Result(probe.ref)
      probe.expectMessage(CalculatorSpec.Total(expectedValue))
    }
  }
}

object CalculatorSpec {

  sealed trait Command
  final case class Add(term: Int) extends Command
  final case class Subtract(term: Int) extends Command
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
        case Result(replyTo) =>
          replyTo ! Total(total)
          Behaviors.same
      }
    })
}