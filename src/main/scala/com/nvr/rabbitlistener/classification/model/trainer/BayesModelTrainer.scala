package com.nvr.rabbitlistener.classification.model.trainer

import com.nvr.rabbitlistener.classification.feature.Feature
import com.nvr.rabbitlistener.classification.label.Label
import com.nvr.rabbitlistener.classification.model.bayes.BayesModel

/**
 * Created by vinay.varma on 11/9/14.
 */
abstract class BayesModelTrainer[FEATURE <: Feature, LABEL <: Label] {
  def countInstances[K](needle: K, haystack: Iterable[K]): Int = {
    haystack.count(_ == needle)
  }

  def getDistribution[K](features: Iterable[K]): Map[K, Float] = {
    features.toSet[K].map(k => k -> countInstances(k, features).toFloat / features.size).toMap
  }

  def train(trainingSet: Iterable[(FEATURE, LABEL)]): BayesModel[FEATURE, LABEL]
}
