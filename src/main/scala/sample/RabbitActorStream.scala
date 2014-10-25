package sample

import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by vinay.varma on 10/5/14.
 */
object RabbitActorStream extends App {
  val conf = new SparkConf().setAppName("Simple Application")
  val sc = new SparkContext(conf)
  val ssc = new StreamingContext(sc, Seconds(1))
  //  ssc.actorStream[String](Props(new RabbitActor()), "CustomReceiver")

}
