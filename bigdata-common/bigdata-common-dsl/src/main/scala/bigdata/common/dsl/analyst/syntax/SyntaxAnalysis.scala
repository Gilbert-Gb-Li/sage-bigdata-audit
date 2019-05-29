package bigdata.common.dsl.analyst.syntax

import java.util.regex.Pattern

import bigdata.common.dsl.analyst.DslAnalysis.{isBlank, isStartExpression, isStopExpression}
import bigdata.common.dsl.analyst.lexer.Token
import bigdata.common.dsl.analyst.vo.{DirectConfig, DslConfig, MethodConfig, NilDirectConfig}
import bigdata.common.log4j2.Logging

import scala.collection.mutable.ListBuffer

case class SyntaxAnalysis(tokens: Seq[Token], dsl: String) extends Logging {
  private var currentConfig: DslConfig = _
  private var mode = SyntaxMode.Normal

  private val numberPattern = Pattern.compile("((\\d+)|(\\d+\\.)|(\\d+\\.\\d+)|(\\.\\d+))")

  private def processNormal(token: Token, index: Int): Boolean = {
    token.data match {
      case x if isBlank(x) =>
      // 空白，代码可读

      case x if isStartExpression(x) => // ( s-表达式开始
        mode = SyntaxMode.StartExpression

      case x if x.startsWith("'") => //  字符串, 直接结束
        if (x.endsWith("'")) {
          currentConfig match {
            case null =>
              currentConfig = new DirectConfig(token, null)
            case x: MethodConfig =>
              currentConfig = new DirectConfig(token, x)
            case x: DirectConfig =>
              logger.error(s"only allow one expression, token:$x, row:${token.stopRow}, col:${token.stopCol}")
              return false
          }
        } else {
          logger.error(s"string missing end ', token:$x, row:${token.stopRow}, col:${token.stopCol}")
          return false
        }
      case x =>
        if (numberPattern.matcher(x).matches()) {
          currentConfig match {
            case null =>
              currentConfig = DirectConfig(token, null)
            case x: MethodConfig =>
              currentConfig = DirectConfig(token, x)
            case x: DirectConfig =>
              logger.error(s"only allow one expression, token:$x, row:${token.stopRow}, col:${token.stopCol}")
              return false
          }
        } else {
          logger.error(s"direct variable is not string and number, token:$x, row:${token.stopCol}, col:${token.stopCol}")
        }
    }
    true
  }

  private def processParam(token: Token, index: Int): Boolean = {
    if (currentConfig == null || !currentConfig.isInstanceOf[MethodConfig]) {
      logger.error(s"invoke fail, only mode=expression can set mode=Param")
      return false
    }
    val methodConfig = currentConfig.asInstanceOf[MethodConfig]
    token.data match {
      case x if isBlank(x) => // 空白，代码可读

      case x if isStopExpression(x) => // ( s-表达式结束
        if (methodConfig.parent == null) {
          mode = SyntaxMode.Over
        } else {
          currentConfig = methodConfig.parent
          mode = SyntaxMode.Param
        }

      case x if x.startsWith("'") => //  字符串
        if (x.endsWith("'")) {
          methodConfig.params += new DirectConfig(token, methodConfig)
        } else {
          logger.error(s"string missing end ', row:${token.stopRow}, col:${token.stopRow}")
          return false
        }
      case x if isStartExpression(x) => // 参数是s-表达式
        mode = SyntaxMode.StartExpression
      case _ => // 直接量
        methodConfig.params += DirectConfig(token, methodConfig)
    }
    true
  }

  private def processStartExpression(token: Token, index: Int): Boolean = {
    token.data match {
      case x if isBlank(x) => // 空白，代码可读
        mode = SyntaxMode.EmptyExpression
      case x if isStopExpression(x) => // 匹配()
        currentConfig match {
          case null =>
            currentConfig = NilDirectConfig(token, null)
          case x: MethodConfig =>
            x.params += NilDirectConfig(token, x)
          case x: DirectConfig =>
            logger.error(s"only allow one expression, token:$x, row:${token.stopRow}, col:${token.stopCol}")
            return false
        }
      case x => // (date "xx")匹配date
        currentConfig match {
          case null =>
            currentConfig = MethodConfig(token, ListBuffer[DslConfig](), null)
          case x: MethodConfig =>
            val config = MethodConfig(token, ListBuffer[DslConfig](), x)
            x.params += config
            currentConfig = config
          case x: DirectConfig =>
            logger.error(s"only allow one expression, token:$x, row:${token.stopRow}, col:${token.stopCol}")
            return false
        }
        mode = SyntaxMode.Param
    }
    true
  }

  def processEmptyExpression(token: Token, index: Int): Boolean = {
    token.data match {
      case x if isBlank(x) => // 空白，代码可读
        mode = SyntaxMode.EmptyExpression
      case x if isStopExpression(x) => // 匹配()
        currentConfig match {
          case null =>
            currentConfig = NilDirectConfig(token, null)
          case methodConfig: MethodConfig =>
            methodConfig.params += NilDirectConfig(token, methodConfig)
          case directConfig: DirectConfig =>
            logger.error("programe error!")
            return false
        }
        mode = SyntaxMode.Over
    }
    true
  }

  def processOver(word: Token, index: Int): Boolean = {
    // 只允许一个S表达式
    if (!isBlank(word.data)) {
      logger.error(s"more than one s-exprssion, token:${word.data}, row:${word.stopRow}, col:${word.stopCol} ")
      return false
    }
    true
  }


  def process(): DslConfig = {
    tokens.indices.foreach(index => {
      val token = tokens(index)
      val res = mode match {
        case SyntaxMode.Normal =>
          processNormal(token, index)
        case SyntaxMode.StartExpression =>
          processStartExpression(token, index)
        case SyntaxMode.EmptyExpression =>
          processEmptyExpression(token, index)
        case SyntaxMode.Param =>
          processParam(token, index)
        case SyntaxMode.Over =>
          processOver(token, index)
      }
      if (!res) {
        return null
      }
    })

    currentConfig
  }


}
