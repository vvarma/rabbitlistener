package com.nvr.costreamer

import com.fasterxml.jackson.databind.{JsonNode, ObjectMapper}
import com.nvr.costreamer.feeder.RabbitFeederConfig
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.StreamingContext._
import org.apache.spark.streaming.{Minutes, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by vinay.varma on 10/3/14.
 */
object SparkHdfs {
  val checkpoint_directory = "hdfs://localhost:54310/tmp/spark/checkpoint"
  val mapper = new ObjectMapper()

  def updateFunc(newValues: Seq[Iterable[String]], currentState: Option[Iterable[String]]): Option[Iterable[String]] = {
    var fullList = Set[String]()
    if (currentState.nonEmpty)
      currentState.get.foreach(s => fullList += s)
    newValues.foreach(ls => ls.foreach(s => fullList += s))
    Some(fullList)
  }

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("Simple Application")
    conf.setMaster("local[4]")
    val sc = new SparkContext(conf)
    val ssc = new StreamingContext(sc, Minutes(1))
    ssc.checkpoint(checkpoint_directory)
    val rabbitConfig = new RabbitFeederConfig("localhost", "europa-statebus", "wayward-listener")
    val jsonStream = ssc.actorStream[String](StreamActor.props(rabbitConfig), "streamActor")


    val idToJsonCoMap = jsonStream.map(json => {
      val node = mapper.readTree(json)
      val key = node.get("id").asText()
      (key, json)
    }).groupByKey()

    val updatedIdToJsonMap = idToJsonCoMap.updateStateByKey(updateFunc)

    val updatedIdToCo = updatedIdToJsonMap.map(tup => {
      val coNodes = for (json <- tup _2) yield mapper.readTree(json)
      (tup _1, coNodes)
    })

    val nonTerminalCO = updatedIdToCo.map(tup => {
      (tup _1, for (node <- tup _2) yield node.get("state").asText())
    }).filter(fft)


    nonTerminalCO.foreachRDD(rdd => rdd.foreach(idTuple => println("Non Terminal " + idTuple._1)))
    updatedIdToJsonMap.saveAsTextFiles("hdfs://localhost:54310/checkout/co/", ".co")
    ssc.start() // Start the computation

    ssc.awaitTermination()
    ssc.stop()

  }

  def parseJsonRDD(jsonRdd: RDD[String]): RDD[JsonNode] = {
    jsonRdd.map[JsonNode](parseJson)
  }

  def parseJson(json: String): JsonNode = {
    mapper.readTree(json)
  }

  def hasNonTerminalState(id: String, states: Iterable[String]): Boolean = {
    !states.exists(s => s == "CHECKOUT_INVALIDATED" || s == "CHECKOUT_EXECUTION_COMPLETED")
  }

  val ff = hasNonTerminalState _
  val fft = ff.tupled


}
