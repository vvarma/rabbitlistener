package com.nvr.costreamer.model

/**
 * Created by vinay.varma on 11/6/14.
 */

object BayesianAccountModel {

  def train(input: Iterable[( CategoryFeature,String)]): BayesianAccountModel = {
    new BayesianAccountModel(BayesModel.getModel[CategoryFeature, String](input))
  }

}

class BayesianAccountModel(model: Map[CategoryFeature, ProbabilisticOutcome[String]]) extends BayesModel[CategoryFeature, String](model)
