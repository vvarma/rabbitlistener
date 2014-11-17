package com.nvr.costreamer.model

/**
 * Created by vinay.varma on 11/1/14.
 */
object BayesModel {
  def countInstances[K](needle: K, haystack: Iterable[K]): Int = {
    var count = 0
    for (hay <- haystack) {
      if (needle == hay) count = count + 1
    }
    count
  }

  def getDistribution[K](features: Iterable[K]): Map[K, Float] = {
    features.toSet[K].map(k => k -> countInstances(k, features).toFloat / features.size).toMap
  }

  def getLikelihood[K, V](input: Iterable[(K, V)]): Map[(K, V), Float] = {
    val distribution = getDistribution(for (i <- input) yield i _1)
    val complDist = getDistribution(input)
    complDist.map(entry => (entry._1, entry._2 * distribution.get(entry._1._1).get))
  }

  def train[K <: Feature, V](input: Iterable[(K, V)]): Map[K, Map[V, Float]] = {
    val distribution = getDistribution(for ((k, v) <- input) yield k)
    val complDist = getDistribution(input)

    val abc = for (k <- for (i <- input) yield i._1) yield {
      (k, {
        for (t <- complDist.filter(p => p._1._1 == k)) yield (t._1._2, t._2 * distribution.get(t._1._1).get)
      })
    }
    abc.toMap

  }

  def getModel[Input <: Feature, Type](input: Iterable[(Input, Type)]): Map[Input, ProbabilisticOutcome[Type]] = {
    for ((feature, probOutcomeMap) <- train(input)) yield (feature, new ProbabilisticOutcome[Type](probOutcomeMap))
  }


}

abstract class BayesModel[Input <: Feature, Type](val model: Map[Input, ProbabilisticOutcome[Type]]) extends ProbabilisticModel[Input, Type] {

  override def predict(input: Input): Option[ProbabilisticOutcome[Type]] = model.get(input)

}

object Main {
  def main(args: Array[String]) {
    val trainFeature = List((new CategoryFeature("a"), "ad1"), (new CategoryFeature("a"), "ad1"), (new CategoryFeature("b"), "ad1"),
      (new CategoryFeature("b"), "ad1"), (new CategoryFeature("c"), "ad1"), (new CategoryFeature("c"), "ad2"),
      (new CategoryFeature("z"), "ad3"), (new CategoryFeature("z"), "ad1"), (new CategoryFeature("a"), "ad2")
    )
    var model = BayesModel.getModel(trainFeature)

    print(model)

  }

}