package bigdata.audit.vo.audit.dsl.config.logic

import bigdata.audit.vo.audit.dsl.Condition

abstract class BaseLogicCondition(override val `type`: String,
                                  val conditions: Seq[Condition])
  extends Condition(`type`) {
  override def getIndicators: Seq[String] = {
    conditions.flatMap(_.getIndicators)
  }
}