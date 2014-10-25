package sample

import akka.actor.{Actor, ActorSystem, Props}
import com.nvr.costreamer.feeder.{CoFeederConfig, Feeder, StartFeeder, SubscribeFeeder}
import org.apache.spark.streaming.receiver.ActorHelper
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by vinay.varma on 10/20/14.
 */
case class StartReceiver(fileName: String)

class SimpleHDFS extends Actor with ActorHelper {
  val conf = new SparkConf().setAppName("Simple HDFS")
  conf.setMaster("local[2]")
  val sc = new SparkContext(conf)

  override def receive: Receive = {
    case StartReceiver(fileName) =>
      val rdd = sc.textFile(fileName)
      rdd.foreach(print)


  }
}

object SimpleHDFS {
  def main(args: Array[String]) {
    val as = ActorSystem.create()
    val rx = as.actorOf(Props[SimpleHDFS])
    rx ! StartReceiver("hdfs://localhost:54310/checkout/raw/checkout.out")
    Thread.sleep(10000)
  }

}

object SlightlyMoreComplexHDFS {
  def main(args: Array[String]) {
    val as = ActorSystem.create()
    val complexHDFS = as.actorOf(Props[SlightlyMoreComplexHDFS])
    val feeder = as.actorOf(Feeder.props(new CoFeederConfig("hdfs://localhost:54310/checkout/raw/checkout.out")))
    val feederSelection = as.actorSelection(feeder.path)
    feederSelection ! SubscribeFeeder(complexHDFS)
    feederSelection ! StartFeeder()
    Thread.sleep(10000)
  }
}

class SlightlyMoreComplexHDFS extends Actor {
  override def receive: Actor.Receive = {
    case json => println("got")
  }
}
