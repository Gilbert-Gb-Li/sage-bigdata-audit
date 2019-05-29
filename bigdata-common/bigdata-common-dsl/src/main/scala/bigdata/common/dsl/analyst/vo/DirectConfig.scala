package bigdata.common.dsl.analyst.vo

import java.util.regex.Pattern

import bigdata.common.dsl.analyst.lexer.Token

class DirectConfig(val token: Token, val parent: MethodConfig)
  extends DslConfig {

  def getRealValue: (Boolean, Any) = {
    val value = token.data
    if (value.startsWith("\"") || value.startsWith("'")) {
      return (true, value.substring(1, value.length - 1))
    }
    val numberPattern = Pattern.compile("((\\d+)|(\\d+\\.)|(\\d+\\.\\d+)|(\\.\\d+))")

    if (numberPattern.matcher(value).matches()) {
      if (value.contains(".")) {
        return (true, value.toDouble)
      } else {
        return (true, value.toLong)
      }
    }
    (false, null)
  }

  override def toString: String = token.data
}

object DirectConfig {
  def apply(value: Token, parent: MethodConfig): DirectConfig = {
    new DirectConfig(value, parent)
  }
}
