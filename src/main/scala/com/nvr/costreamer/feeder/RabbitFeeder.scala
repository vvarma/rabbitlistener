package com.nvr.costreamer.feeder

import akka.actor.ActorRef
import com.rabbitmq.client.{ConnectionFactory, QueueingConsumer}

/**
 * Created by vinay.varma on 10/19/14.
 */
class RabbitFeeder(val config: RabbitFeederConfig) extends Feeder {
  private val connection = {
    val connectionFactory = new ConnectionFactory
    connectionFactory.setHost(config.host)
    //    connectionFactory.setPort(15672)
    //    connectionFactory.setUsername(userName)
    //    connectionFactory.setPassword(password)
    connectionFactory.newConnection()
  }


  def listen() {
    val channel = connection.createChannel()
    channel.exchangeDeclare(config.exchange, "direct", true)
    channel.queueDeclare(config.queue, true, false, false, null)
    channel.queueBind(config.queue, config.exchange, "")
    println("Waiting for messages. on queue :" + config.queue)
    val consumer = new QueueingConsumer(channel)
    channel.basicConsume(config.queue, true, consumer)
    while (true) {
      val delivery = consumer.nextDelivery()
      feed(new String(delivery.getBody))
    }

  }

  override def receive: Receive = {
    case SubscribeFeeder(receiverActor: ActorRef) =>
      println("received subscribe from %s".format(receiverActor.toString))
      receivers += receiverActor

    case UnsubscribeFeeder(receiverActor: ActorRef) =>
      println("received unsubscribe from %s".format(receiverActor.toString))
      receivers -= receiverActor
    case StartFeeder =>
      new Thread() {
        override def run(): Unit = {
          listen()
        }
      }.start()
  }

}
