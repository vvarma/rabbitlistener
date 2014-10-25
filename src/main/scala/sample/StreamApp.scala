package sample

import akka.actor.{ActorSystem, Props}
import com.nvr.costreamer.RabbitActor
import org.apache.spark.streaming.{Minutes, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by vinay.varma on 10/3/14.
 */
object StreamApp {
  def main(args: Array[String]) {
    val system = ActorSystem()
    val logFile = "file:////Users/vinay.varma/workspace/rabbitlistener/crap/cr" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local[4]")
    val sc = new SparkContext(conf)

    val ssc = new StreamingContext(sc, Minutes(5))
    val lines = ssc.actorStream[String](
      Props(classOf[RabbitActor]),
      "SampleReceiver"
    )

    lines.filter(f => {
      !f.isEmpty
    }).saveAsTextFiles(logFile, "cd.txt")
    ssc.start() // Start the computation

    ssc.awaitTermination() // Wait for the computation to terminate

  }

}
