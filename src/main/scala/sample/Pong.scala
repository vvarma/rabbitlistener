package sample

import akka.actor.Actor

/**
 * Created by vinay.varma on 10/19/14.
 */
class Pong extends Actor {
  def receive = {
    case PingMessage =>
      println("  pong")
      sender ! PongMessage
    case StopMessage =>
      println("pong stopped")
      context.stop(self)
  }
}
