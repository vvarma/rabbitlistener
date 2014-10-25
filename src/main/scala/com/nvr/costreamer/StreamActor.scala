package com.nvr.costreamer

import akka.actor.IO.Done
import akka.actor.{Actor, Props}
import com.nvr.costreamer.feeder._
import org.apache.spark.streaming.receiver.ActorHelper

/**
 * Created by vinay.varma on 10/19/14.
 */
object StreamActor {
  def props(config:FeederConfig): Props = Props(new StreamActor(config))
}

class StreamActor(config:FeederConfig) extends Actor with ActorHelper {
  //implicit val resolveTimeout = Timeout(5, TimeUnit.SECONDS)
  //  val feeder = Await.result(context.system.actorSelection(feederConfig.actorPath).resolveOne(), resolveTimeout.duration)

  val feeder = context.actorOf(Feeder.props(config))

  feeder ! SubscribeFeeder(context.self)
  feeder ! StartFeeder

  //  feeder ! StartFeeder("/Users/vinay.varma/Documents/dumps/checkout.out")

  override def preStart(): Unit = {
    println("started actor stream wala")

  }

  override def receive: Receive = {
    case Done =>
      sender ! UnsubscribeFeeder
    // stop stream
    case jsonCo =>
      println("Stored")
      store(jsonCo)
  }
}