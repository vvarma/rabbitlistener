package com.nvr.costreamer.model

import com.fasterxml.jackson.databind.JsonNode
import org.apache.spark.mllib.linalg.DenseVector
import org.apache.spark.mllib.regression.LabeledPoint

/**
 * Created by vinay.varma on 10/25/14.
 */
object CheckoutState {
  def create(node: JsonNode): CheckoutState = {
    val state: String = node.get("state").asText()
    val orderComplete: Boolean = node.get("state").asText() == "CHECKOUT_EXECUTION_COMPLETED"
    val coNode = node.get("checkout").get("dataSet").get("availableData")
    var pincode: String = null
    var accountId: String = null
    var addressId: String = null
    var billingAmount: Int = 0
    var categoryNodeIdSet = Set[Int]()
    var paymentMethods: List[(String, String)] = null
    var giftwrap: Boolean = false

    if (coNode.has("CLIENT_ACCOUNT_INFO_DATA")) {
      val clientNode = coNode.get("CLIENT_ACCOUNT_INFO_DATA")
      if (clientNode.has("pincode")) pincode = clientNode.get("pincode").asText()
      if (clientNode.has("accountId")) accountId = clientNode.get("accountId").asText()
      if (clientNode.has("addressId")) addressId = clientNode.get("addressId").asText()
    }

    if (coNode.has("OMS_ORDER")) {
      val orderNode = coNode.get("OMS_ORDER")
      if (orderNode.has("billingInfo") && orderNode.get("billingInfo").has("billingAmount")) billingAmount = orderNode.get("billingInfo").get("billingAmount").asInt()
      if (orderNode.has("vasData") && orderNode.get("vasData").has("giftWrap") && orderNode.get("vasData").get("giftWrap").has("hasCustomerOpted"))
        giftwrap = orderNode.get("vasData").get("giftWrap").get("hasCustomerOpted").asBoolean()

      if (orderNode.has("orderItems")) {
        val orderItems = orderNode.get("orderItems")
        val orderItemsIterator = orderItems.iterator()
        while (orderItemsIterator.hasNext) {
          val orderItem = orderItemsIterator.next()
          categoryNodeIdSet += orderItem.get("cmsData").get("categoryId").asText().toInt
        }

        //        categoryNodeIdSet = for (orderItem: JsonNode <- orderItems) yield orderItem.get("cmsData").get("categoryId").asText().toInt

      }


    }


    if (coNode.has("INITIATED_PAYMENTS")) {
      val initPayments = coNode.get("INITIATED_PAYMENTS")
      //paymentMethods = for (payment: java.util.Map.Entry[String, JsonNode] <- initPayments.get("allPayments").fields()) yield
      //(payment.getValue.get("paymentMethod").asText(), payment.getValue.get("status").asText())

    }
    new CheckoutState(accountId, addressId, billingAmount, pincode, orderComplete, state, categoryNodeIdSet.toList, paymentMethods, giftwrap)
  }
}

class CheckoutState(val accountId: String, val addressId: String,
                    val billingAmount: Int, val pincode: String,
                    val orderComplete: Boolean, val state: String,
                    val categoryNodeIds: List[Int], val paymentMethods: List[(String, String)],
                    val giftwrap: Boolean) extends Serializable with Feature {
  def genericTuple = {
    (accountId ne null, addressId ne null, billingAmount, orderComplete)
  }

  def labelPoint = {
    val abc: Array[Double] = Array(getDoube(accountId.isEmpty), getDoube(addressId.isEmpty), billingAmount, getDoube(pincode.isEmpty), categoryNodeIds.size, getDoube(giftwrap))
    new LabeledPoint(getDoube(orderComplete), new DenseVector(abc))

  }

  def getDoube(bool: Boolean) = if (bool) 1.0 else 0.0

  override def toString: String = {
    accountId
  }
}

//enumerate state
//pincode -> urban:boolean city:enumerate

