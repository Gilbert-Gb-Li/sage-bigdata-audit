package bigdata.audit.vo.audit.dsl.compare

case class GeCondition(override val param1: CompareIndicator,
                       override val param2: CompareIndicator)
  extends CompareCondition(">=", param1, param2)
