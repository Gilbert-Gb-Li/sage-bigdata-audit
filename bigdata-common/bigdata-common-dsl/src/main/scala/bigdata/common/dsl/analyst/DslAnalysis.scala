package bigdata.common.dsl.analyst

import java.util.regex.Pattern

object DslAnalysis {
  private val blankPattern = Pattern.compile("[\\s\r\n\\t]+")

  def isBlank(c: Char): Boolean = {
    isBlank(String.valueOf(c))
  }


  def isBlank(c: String): Boolean = {
    blankPattern.matcher(c).matches()
  }

  val oneCharWord = "()"

  def isOneCharWord(c: Char): Boolean = {
    if (oneCharWord.contains(c)) {
      return true
    }
    false
  }

  def isStartExpression(c: String): Boolean = {
    c == "("
  }

  def isStopExpression(c: String): Boolean = {
    c == ")"
  }

  def isOneCharWord(c: String): Boolean = {
    if (oneCharWord.contains(c)) {
      return true
    }
    false
  }
}
