package com.nvr.rabbitlistener.classification.model.bayes

import com.nvr.rabbitlistener.classification.feature.Feature
import com.nvr.rabbitlistener.classification.label.Label
import com.nvr.rabbitlistener.classification.model.GenericClassificationModel

/**
 * Created by vinay.varma on 11/9/14.
 */
class BayesModel[FEATURE <: Feature, LABEL <: Label](val probableOutcomes: Map[FEATURE, Map[LABEL, Float]]) extends GenericClassificationModel[FEATURE, LABEL] {
  override def predict(feature: FEATURE): Option[LABEL] = {
    if (probableOutcomes.get(feature).isDefined) {
      Some(probableOutcomes.get(feature).get.maxBy(_._2)._1)
    } else None
  }
}

