package com.nvr.costreamer.feeder

import akka.actor.ActorRef
import org.apache.spark.{SparkConf, SparkContext}


class CoFeeder(val config: CoFeederConfig) extends Feeder {
  val conf = new SparkConf().setAppName("CO-FEEDER")
  conf.setMaster("local[2]")
  val sc = new SparkContext(conf)

  override def receive: Receive = {
    case SubscribeFeeder(listener: ActorRef) =>
      println("registered listener: " + listener.path + " for feeder: " + self.path)
      receivers += listener
    case StartFeeder =>
      println("Starting feeder")
      startFeeder(config.fileName)
    case UnsubscribeFeeder(listener: ActorRef) =>
      println("unregistered listener")
      receivers -= listener
  }

  def startFeeder(fileName: String): Unit = {
    val rdd = sc.textFile(fileName)
    val cos = rdd.collect()
    println("Feeding")
    cos.foreach(feed)
    stopFeeder()
  }
}
