package com.nvr.rabbitlistener.classification

/**
 * Created by vinay.varma on 11/11/14.
 */
class NonTerminalCoStream(val coStream: CoStreamer) {
  val nonTerminalCO = coStream.updatedIdToCo.map(tup => {
    (tup _1, for (node <- tup _2) yield node.get("state").asText())
  }).filter(fft)

  def hasNonTerminalState(id: String, states: Iterable[String]): Boolean = {
    !states.exists(s => s == "CHECKOUT_INVALIDATED" || s == "CHECKOUT_EXECUTION_COMPLETED")
  }

  val ff = hasNonTerminalState _
  val fft = ff.tupled

}
