package com.nvr.rabbitlistener.classification.model

import com.nvr.rabbitlistener.classification.feature.Feature
import com.nvr.rabbitlistener.classification.label.Label

/**
 * Created by vinay.varma on 11/9/14.
 */

trait GenericClassificationModel[FEATURE <: Feature, LABEL <: Label] {
  def predict(feature: FEATURE): Option[LABEL]
}
