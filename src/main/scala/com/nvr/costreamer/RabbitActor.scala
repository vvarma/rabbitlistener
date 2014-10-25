package com.nvr.costreamer

import akka.actor.{Actor, Props}
import com.nvr.costreamer.feeder.{RabbitFeeder, SubscribeFeeder, UnsubscribeFeeder}
import org.apache.spark.streaming.receiver.ActorHelper

/**
 * Created by vinay.varma on 10/5/14.
 */
class RabbitActor() extends Actor with ActorHelper {
  val remotePublisher = context.system.actorOf(Props(classOf[RabbitFeeder], "localhost", "europa-statebus", "guest", "guest"), name = "RabbitListener")

  override def preStart = remotePublisher ! SubscribeFeeder(context.self)

  override def receive: Receive = {
    case data: String => store(data)
      sender ! "ok"
  }

  override def postStop() = remotePublisher ! UnsubscribeFeeder(context.self)
}
