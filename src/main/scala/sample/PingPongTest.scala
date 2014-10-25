package sample

import akka.actor.{Props, ActorSystem}

/**
 * Created by vinay.varma on 10/19/14.
 */
object PingPongTest extends App {
  val system = ActorSystem("PingPongSystem")
  val pong = system.actorOf(Props[Pong], name = "pong")
  val ping = system.actorOf(Props(new Ping()), name = "ping")
  // start them going
  ping ! StartMessage
}
