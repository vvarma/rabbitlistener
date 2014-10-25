package com.nvr.costreamer

import akka.actor.IO.Done
import akka.actor.{Actor, ActorSystem, Props}
import com.nvr.costreamer.feeder._
import com.rabbitmq.client.ConnectionFactory

/**
 * Created by vinay.varma on 10/24/14.
 */
object RabbitProducerActor {
  val config = new RabbitFeederConfig("localhost", "europa-statebus", "wayward-listener")

  def props(feederConfig: FeederConfig) = Props(new RabbitProducerActor(config, feederConfig))
}

class RabbitProducerActor(val config: RabbitFeederConfig, feederConfig: FeederConfig) extends Actor {
  val feeder = context.system.actorOf(Feeder.props(feederConfig),"feeder")
  feeder ! SubscribeFeeder(context.self)
  feeder ! StartFeeder
  private val connection = {
    val connectionFactory = new ConnectionFactory
    connectionFactory.setHost(config.host)
    //    connectionFactory.setPort(15672)
    //    connectionFactory.setUsername(userName)
    //    connectionFactory.setPassword(password)
    connectionFactory.newConnection()
  }
  val channel = connection.createChannel()
  channel.exchangeDeclare(config.exchange, "direct", true)
  channel.queueDeclare(config.queue, true, true, true, null)
  channel.queueBind(config.queue, config.exchange, "")

  def pushToRMQ(json: String) {
    channel.basicPublish(config.exchange, "", null, json.getBytes)
  }

  override def receive: Receive = {
    case Done =>
      println("Done!")
      sender ! UnsubscribeFeeder
      case json: String =>
      pushToRMQ(json)
  }
}

object Runner {
  def main(args: Array[String]) {
    val as = ActorSystem.create()
    val feederConfig = new CoFeederConfig("hdfs://localhost:54310/checkout/raw/checkout.out")
    as.actorOf(RabbitProducerActor.props(feederConfig), "rmq-enqueue")

  }
}