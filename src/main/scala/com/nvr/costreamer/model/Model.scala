package com.nvr.costreamer.model

/**
 * Created by vinay.varma on 11/5/14.
 */

trait Feature extends Serializable

abstract class Outcome[Type, Result] extends Serializable

class ProbabilisticOutcome[Type](val outcomes: Map[Type, Float]) extends Outcome[Type, Float] {
  override def toString: String = outcomes.toString()

}

abstract class Model[Input <: Feature, Type, Result] {
  def predict(input: Input): Option[Outcome[Type, Result]]
}

abstract class ProbabilisticModel[Input <: Feature, Type] extends Model[Input, Type, Float] {
  def predict(input: Input): Option[ProbabilisticOutcome[Type]]
}
