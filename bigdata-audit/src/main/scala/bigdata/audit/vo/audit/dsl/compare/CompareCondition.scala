package bigdata.audit.vo.audit.dsl.compare

import bigdata.audit.vo.audit.dsl.Condition

import scala.collection.mutable.ListBuffer

abstract class CompareCondition(override val `type`: String,
                                val param1: CompareIndicator,
                                val param2: CompareIndicator)
  extends Condition(`type`) {
  override def getIndicators: Seq[String] = {
    var seq = ListBuffer[String]()
    if (param1.indicator != null && param1.indicator.trim.nonEmpty) {
      seq += param1.indicator.trim
    }
    if (param2.indicator != null && param2.indicator.trim.nonEmpty) {
      seq += param2.indicator.trim
    }
    seq.toSeq
  }
}
