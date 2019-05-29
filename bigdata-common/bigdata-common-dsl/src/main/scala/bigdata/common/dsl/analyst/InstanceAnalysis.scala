package bigdata.common.dsl.analyst

import bigdata.common.dsl.DslService
import bigdata.common.dsl.analyst.vo.{DirectConfig, DslConfig, MethodConfig, NilDirectConfig}
import bigdata.common.dsl.method._
import bigdata.common.log4j2.Logging

import scala.collection.mutable.ListBuffer

case class InstanceAnalysis(config: DslConfig,
                            dsl: String)
  extends Logging {

  def instanceDateFormatDslService(x: MethodConfig): DateFormatDslService = {
    if (x.params == null) {
      logger.error(s"(date must with 2 param, real is 0, row:${x.method.stopRow}, col:${x.method.stopCol}")
      return null
    }
    if (x.params.size != 2) {
      logger.error(s"(date must with 2 param, real is ${x.params.size}, row:${x.method.stopRow}, col:${x.method.stopCol}")
      return null
    }

    val field = process(x.params.head)

    val pattern = process(x.params.last)
    DateFormatDslService(field, pattern)
  }

  def instanceFieldMapDslService(x: MethodConfig): DslService = {
    if (x.params == null) {
      logger.error(s"(field must with 1 param, real is 0, row:${x.method.stopRow}, col:${x.method.stopCol}")
      return null
    }
    if (x.params.size != 1) {
      logger.error(s"(field must with 1 param, real is ${x.params.size}, row:${x.method.stopRow}, col:${x.method.stopCol}")
      return null
    }

    val field = process(x.params.head)
    FieldDslService(field)
  }


  def instanceConcatMapDslService(x: MethodConfig): DslService = {
    if (x.params == null) {
      logger.error(s"(date must with 3+ param, real is 0, line(${x.method.stopRow}:${x.method.stopCol})")
      return null
    }


    val params = x.params.map(param => {
      val service = process(param)
      service
    })
    ConcatDslService(params)
  }

  def instanceStaticDslService(x: MethodConfig): DslService = {
    if (x.params == null) {
      logger.error(s"(static must with 1 param, real is 0, row:${x.method.stopRow}, col:${x.method.stopCol}")
      return null
    }
    if (x.params.size != 1) {
      logger.error(s"(static must with 1 param, real is ${x.params.size}, row:${x.method.stopRow}, col:${x.method.stopCol}")
      return null
    }
    if (!checkMustAllDirectConfig(x.params)) {
      logger.error(s"(static must with 1 param, the 1 param must be direct")
      return null
    }
    val field = x.params.head.asInstanceOf[DirectConfig].getRealValue match {
      case (true, x: Any) if x != null =>
        x.toString
      case _ =>
        null
    }
    if (field == null) {
      logger.error(s"$dsl is (static line(${x.method.stopRow}:${x.method.stopCol}) param must not empty")
      return null
    }
    StaticDslService(field)
  }

  def instanceMethodConfig(x: MethodConfig): DslService = {
    x.method.data match {
      case "date-format" =>
        instanceDateFormatDslService(x)
      case "field" =>
        instanceFieldMapDslService(x)
      case "concat" =>
        instanceConcatMapDslService(x)
      case "static" =>
        instanceStaticDslService(x)
      case _ =>
        null
    }
  }

  def checkMustAllDirectConfig(params: ListBuffer[DslConfig]): Boolean = {
    params.forall(_.isInstanceOf[DirectConfig])
  }

  def instanceDirectConfig(config: DirectConfig): DslService = {
    config match {
      case _: NilDirectConfig =>
        NullValueDslService()
      case _ =>
        config.getRealValue match {
          case (true, x) if x != null =>
            StaticDslService(x)
          case _ =>
            throw new Exception("invalid value")
        }
    }
  }

  def process(res: DslConfig): DslService = {
    res match {
      case x: MethodConfig =>
        instanceMethodConfig(x)
      case x: DirectConfig =>
        instanceDirectConfig(x)
    }
  }

}
