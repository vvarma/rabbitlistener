package com.nvr

import com.nvr.rabbitlistener.classification.{BayesianCoStream, CoStreamer}
import org.apache.spark.streaming.{Minutes, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by vinay.varma on 10/3/14.
 */
object StreamRunner {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local[4]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Minutes(1))
    new BayesianCoStream(new CoStreamer(ssc))
    //    new CoStreamer(ssc)
    ssc.start() // Start the computation
    ssc.awaitTermination()
    ssc.stop()

  }


}
