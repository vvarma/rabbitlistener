package com.nvr.costreamer.feeder

import akka.actor.IO.Done
import akka.actor.{Actor, ActorRef, Props}

/**
 * Created by vinay.varma on 10/19/14.
 */
case class SubscribeFeeder(receiverActor: ActorRef)

case class UnsubscribeFeeder(receiverActor: ActorRef)

case class StartFeeder()

object Feeder {
  def props(config: FeederConfig): Props = {
    config match {
      case config: CoFeederConfig => Props(new CoFeeder(config))
      case config: RabbitFeederConfig => Props(new RabbitFeeder(config))
    }
  }
}


abstract class Feeder extends Actor {

  var receivers = Set[ActorRef]()

  def feed(message: AnyRef) {
    receivers.foreach(receiver => receiver ! message)
  }

  def stopFeeder() {
    receivers.foreach(receiver => receiver ! Done)
  }
}
