package com.nvr.rabbitlistener.classification

import com.nvr.costreamer.model.CheckoutState

/**
 * Created by vinay.varma on 11/11/14.
 */
class NaiveBayesCoStream(val coStreamer: CoStreamer) {
  val categoryIds = scala.collection.mutable.Map[String, Int]()
  val coStream = coStreamer.updatedIdToCo.flatMap(tup => for (co <- tup _2) yield CheckoutState.create(co))
  val labelPointStream = coStream.map(co => co.labelPoint)


}
