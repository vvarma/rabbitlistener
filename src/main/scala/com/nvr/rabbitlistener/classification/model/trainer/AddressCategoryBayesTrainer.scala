package com.nvr.rabbitlistener.classification.model.trainer

/**
 * Created by vinay.varma on 11/9/14.
 */
/*
class AddressCategoryBayesTrainer extends BayesModelTrainer[CategoryFeature, String] {
  override def train(trainingSet: Iterable[(CategoryFeature, String)]): BayesModel[CategoryFeature, String] = {
    val prior = getDistribution(trainingSet.map(tup => tup._2))
    val categoryPosterior = getDistribution(trainingSet.map(tup => (tup._1.categoryNodeId, tup._2)))
    val uniqueCategories = Set() ++ trainingSet.map(tup => tup _1)
    for (category <- uniqueCategories) yield {
      categoryPosterior.filter(yup => yup._1._1 == category.categoryNodeId)
    }

  }
}
*/
