package ca.malkov.yuriy.calculator

import akka.actor.testkit.typed.scaladsl.{ActorTestKit, TestProbe}
import akka.actor.typed.{ActorRef, Behavior}
import akka.actor.typed.scaladsl.Behaviors
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.util.Try

class CalculatorFactorySpec
  extends AnyWordSpec
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Matchers {

  val testKit: ActorTestKit = ActorTestKit()

  override def afterAll(): Unit = testKit.shutdownTestKit()

  "CalculatorFactory" must {
    "spawn calculator actor" in {
//      testKit.run()
    }
  }

}

// mocking type of message that CalculatorFactory will receive
// which is value and
case class Operand(i: Int)
case class Message(operand: Operand, replyTo: ActorRef[Try[Int]])

object CalculatorFactorySpec {
  // mock message
}
