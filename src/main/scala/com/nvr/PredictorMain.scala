package com.nvr

import com.nvr.costreamer.model.{CategoryFeature, ProbabilisticOutcome}
import org.apache.spark.SparkContext._
import org.apache.spark.{SparkConf, SparkContext}


/**
 * Created by vinay.varma on 11/14/14.
 */
object PredictorMain {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local[4]")
    val sc = new SparkContext(conf)
    val modelRdd = sc.objectFile[(String, Map[CategoryFeature, ProbabilisticOutcome[String]])]("hdfs://localhost:54310/checkout/mr/mod-1415905440000.like")
    //modelRdd.foreach(print)
    modelRdd.lookup("ACC14073166641744848").foreach(print)

  }

}
