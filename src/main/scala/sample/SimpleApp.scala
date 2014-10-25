package sample

import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by vinay.varma on 10/3/14.
 */
object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "hdfs://localhost:54310/tmp/crap" // Should be some file on your system
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println("Lines with a: %s, Lines with b: %s".format(numAs, numBs))

  }

}
