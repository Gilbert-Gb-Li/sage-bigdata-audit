package bigdata.common.dsl.analyst.lexer

import bigdata.common.dsl.analyst.DslAnalysis.{isBlank, isOneCharWord}
import bigdata.common.dsl.analyst.Mode

import scala.collection.mutable.ListBuffer

case class LexicalAnalysis(val dsl: String, dropEmpty: Boolean = true) {
  private var mode = Mode.Normal
  private var pre: Character = _
  private var row: Int = 0
  private var col: Int = 0
  //
  private val tokens = new ListBuffer[Token]()
  private var token: Token = _


  private def addNewToken(c: Char): Unit = {
    addToken()
    tokens += Token(String.valueOf(c), row, col, row, col)
    token = null
  }

  def addStartToken(c: Char) = {
    addToken()
    token = Token(String.valueOf(c), row, col, row, col)
  }

  private def addToken(): Unit = {
    if (token != null && token.nonEmpty) {
      if (dropEmpty) {
        if (token.data != null && token.data.trim.nonEmpty) {
          tokens += token
        }
      } else {
        tokens += token
      }
    }


    token = null
  }

  def addStopToken(c: Char): Unit = {
    appendChar(c)
    addToken()
  }

  def appendChar(c: Char): Unit = {
    if (token == null) {
      token = Token(String.valueOf(c), row, col, row, col)
    } else {
      val builder = token.data + c

      token = Token(builder, token.startRow, token.startCol, row, col)
    }
  }

  def appendString(c: String): Unit = {
    if (token == null) {
      token = Token(c, row, col, row, col)
    } else {
      val builder = token.data + c
      token = Token(builder, token.startRow, token.startCol, row, col)
    }
  }

  def startEscape(c: Char): Unit = {
    c match {
      case '"' => // 转义
        appendChar(c)
      case _ => // 其它字符，原样输入
        appendString(String.valueOf(pre) + String.valueOf(c))
    }
    mode = Mode.StartString
  }


  def processString(c: Char): Unit = {
    c match {
      case '\\' => // 转义字符
        mode = Mode.StartEscape
      case '"' => // 结束字符
        addStopToken(c)
        mode = Mode.Normal
      case _ => // 普通字符
        appendChar(c)
        mode = Mode.StartString
    }
  }

  def processNormal(c: Char): Unit = {
    c match {
      case _ if isOneCharWord(c) => // 单字符 词
        dealOneCharWord(c)
      case _ if isBlank(c) =>
        dealStartBlank(c)
      case '"' => // 字符串词
        appendChar(c)
        mode = Mode.StartString
      case _ => // 直接量
        appendChar(c)
        mode = Mode.StartStatic
    }
  }


  def processStatic(c: Char) = {
    c match {
      case x if isOneCharWord(x) => // 直接量结束：S表达式()
        dealOneCharWord(c)
      case x if isBlank(x) => // 直接量结束：空白
        addStartToken(c)
        mode = Mode.StartBlank
      case _ =>
        appendChar(c)
        mode = Mode.StartStatic
    }
  }

  def processBlank(c: Char): Unit = {
    c match {
      case x if isBlank(x) => // 空白 -> 空白
        appendChar(c)
        mode = Mode.StartBlank
      case x if isOneCharWord(x) => // 空白 -> ()
        dealOneCharWord(x)
      case '"' => // 空白 -> "
        dealStartString(c)
      case _ => // 直接量
        addStartToken(c)
        mode = Mode.StartStatic
    }

  }

  private def dealStartString(c: Char) = {
    addStartToken(c)
    mode = Mode.StartString
  }

  private def dealStartBlank(x: Char) = {
    addStartToken(x)
    mode = Mode.StartBlank
  }

  private def dealOneCharWord(x: Char) = {
    addNewToken(x)
    mode = Mode.Normal
  }


  def process(dsl: String): Seq[Token] = {
    dsl.foreach(c => {
      if (c == '\n') {
        row += 1
        col = 0
      } else {
        col += 1
      }
      mode match {
        case Mode.Normal =>
          processNormal(c)
        case Mode.StartString =>
          processString(c)
        case Mode.StartEscape =>
          startEscape(c)
        case Mode.StartStatic =>
          processStatic(c)
        case Mode.StartBlank =>
          processBlank(c)
      }
      pre = c
    })
    tokens
  }


}
