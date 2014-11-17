package com.nvr.costreamer.model

/**
 * Created by vinay.varma on 11/7/14.
 */
class CategoryFeature(val categoryNodeId: String) extends Feature {


  def canEqual(other: Any): Boolean = other.isInstanceOf[CategoryFeature]

  override def equals(other: Any): Boolean = other match {
    case that: CategoryFeature =>
      (that canEqual this) &&
        categoryNodeId == that.categoryNodeId
    case _ => false
  }

  override def hashCode(): Int = {
    val state = Seq(categoryNodeId)
    state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
  }

  override def toString: String = "%s".format(categoryNodeId)


}
