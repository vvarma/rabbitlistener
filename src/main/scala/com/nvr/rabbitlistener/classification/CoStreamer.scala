package com.nvr.rabbitlistener.classification

import com.fasterxml.jackson.databind.ObjectMapper
import com.nvr.costreamer.StreamActor
import com.nvr.costreamer.feeder.RabbitFeederConfig
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.StreamingContext._

/**
 * Created by vinay.varma on 11/11/14.
 */
class CoStreamer(val ssc: StreamingContext) extends Serializable {
  val checkpoint_directory = "hdfs://localhost:54310/tmp/spark/checkpoint"

  ssc.checkpoint(checkpoint_directory)

  val rabbitConfig = new RabbitFeederConfig("localhost", "europa-statebus", "wayward-listener")

  val jsonStream = ssc.actorStream[String](StreamActor.props(rabbitConfig), "streamActor")
  val idToJsonCoMap = jsonStream.map(json => {
    val node = new ObjectMapper().readTree(json)
    val key = node.get("id").asText()
    (key, json)
  }).groupByKey()
  val updatedIdToJsonMap = idToJsonCoMap.updateStateByKey(StreamerUtils.updateFunc)

  val updatedIdToCo = updatedIdToJsonMap.map(tup => {
    val coNodes = for (json <- tup _2) yield new ObjectMapper().readTree(json)
    (tup _1, coNodes)
  })

  updatedIdToJsonMap.saveAsTextFiles("hdfs://localhost:54310/checkout/co/", ".co")

  val stream= ssc.fileStream()
}

object StreamerUtils {
  def updateFunc(newValues: Seq[Iterable[String]], currentState: Option[Iterable[String]]): Option[Iterable[String]] = {
    var fullList = Set[String]()
    if (currentState.nonEmpty)
      currentState.get.foreach(s => fullList += s)
    newValues.foreach(ls => ls.foreach(s => fullList += s))
    Some(fullList)
  }
}
