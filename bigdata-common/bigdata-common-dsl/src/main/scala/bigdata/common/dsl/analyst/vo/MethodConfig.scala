package bigdata.common.dsl.analyst.vo

import bigdata.common.dsl.analyst.lexer.Token

import scala.collection.mutable.ListBuffer

case class MethodConfig(method: Token,
                        params: ListBuffer[DslConfig],
                        parent: MethodConfig)
  extends DslConfig {
  override def toString: String = {
    val tmp = params.map(_.toString).mkString(" ")
    s"(${method.data} $tmp)"
  }


}
