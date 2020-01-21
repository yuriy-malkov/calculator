package ca.malkov.yuriy.calculator

import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object CalculatorActor {

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