package bigdata.common.dsl.analyst.vo

import bigdata.common.dsl.analyst.lexer.Token

case class NilDirectConfig(override val token: Token,
                           override val parent: MethodConfig)
  extends DirectConfig(token, parent) {
  override def toString: String = "nil"

}
