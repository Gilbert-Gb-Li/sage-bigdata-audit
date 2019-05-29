package bigdata.audit.utils

import scala.beans.BeanProperty


object VarianceUtils {

  case class Variance(@BeanProperty sum: Double,
                      @BeanProperty avg: Double,
                      @BeanProperty variance: Double)

  case class StandardDeviation(@BeanProperty sum: Double,
                               @BeanProperty avg: Double,
                               @BeanProperty variance: Double,
                               @BeanProperty std: Double)

  def variance(data: Seq[Double]): Variance = {
    val sum: Double = data.sum
    val avg: Double = sum / data.length
    val variance = data.map(item => {
      val tmp = item - avg
      tmp * tmp
    }).sum / data.length
    Variance(sum, avg, variance)
  }

  def std(data: Seq[Double]): StandardDeviation = {
    val sum: Double = data.sum
    val avg: Double = sum / data.length
    val variance: Double = data.map(item => {
      val tmp = item - avg
      tmp * tmp
    }).sum / data.length

    StandardDeviation(sum, avg, variance, Math.sqrt(variance))
  }
}
