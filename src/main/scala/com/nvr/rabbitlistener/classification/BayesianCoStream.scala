package com.nvr.rabbitlistener.classification

import com.nvr.costreamer.model.{BayesModel, CategoryFeature, CheckoutState}
import org.apache.spark.streaming.StreamingContext._


/**
 * Created by vinay.varma on 11/11/14.
 */
class BayesianCoStream(val coStreamer: CoStreamer) {
  val coStream = coStreamer.updatedIdToCo.flatMap(tup => for (co <- tup _2) yield CheckoutState.create(co))

  val bayesAddressStream = coStream.map(co => (co.accountId, co)).groupByKey()

  
//  coStream.flatMap(co=>co.categoryNodeIds).foreachRDD(rdd=>rdd.foreach(cNodeId=>))
  /*bayesAddressStream.map(tup => {
    val addrs = for (co <- tup _2) yield co.addressId
    (tup _1, BayesModel.getDistribution(addrs))
  }).saveAsTextFiles("hdfs://localhost:54310/checkout/mr/ba", ".dist")

  bayesAddressStream.map(tup => {
    val trainingSet = for (co <- tup _2) yield (co.addressId, if (co.categoryNodeIds.isEmpty) 0 else co.categoryNodeIds.head)
    (tup _1, BayesModel.getLikelihood(trainingSet))
  }).saveAsObjectFiles("hdfs://localhost:54310/checkout/mr/ca", "like")*/

  val modelStream=bayesAddressStream.map(tup => {
    val trainingSet = for (co <- tup _2) yield {
      if (co.categoryNodeIds.isEmpty) (new CategoryFeature(""), co.addressId) else (new CategoryFeature(co.categoryNodeIds.head.toString), co.addressId)
    }
    (tup _1, BayesModel.getModel(trainingSet))
  })

  modelStream.saveAsObjectFiles("hdfs://localhost:54310/checkout/mr/mod", "like")





}
