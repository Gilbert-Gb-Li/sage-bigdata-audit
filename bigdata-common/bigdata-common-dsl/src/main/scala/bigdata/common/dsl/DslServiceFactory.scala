package bigdata.common.dsl

import bigdata.common.dsl.analyst.InstanceAnalysis
import bigdata.common.dsl.analyst.lexer.LexicalAnalysis
import bigdata.common.dsl.analyst.syntax.SyntaxAnalysis
import bigdata.common.dsl.method._
import bigdata.common.log4j2.Logging

import scala.util.{Failure, Success, Try}


object DslServiceFactory extends Logging {


  def parse(dsl: String): DslService = Try {
    if (dsl == null) {
      return null
    }
    val tmp = dsl.trim
    if (!tmp.startsWith("(")) {
      return StaticDslService(tmp)
    }
    val lexicalAnalyst = LexicalAnalysis(tmp)
    val tokens = lexicalAnalyst.process(dsl)
    if (tokens == null || tokens.isEmpty) {
      return null
    }
    val syntaxAnalysis = SyntaxAnalysis(tokens, dsl)
    val res = syntaxAnalysis.process()
    if (res == null) {
      return null
    }
    val instanceAnalysis = InstanceAnalysis(res, dsl)
    instanceAnalysis.process(res)
  } match {
    case Success(x) =>
      x
    case Failure(e) =>
      logger.error(s"parse dsl fail:$dsl", e)
      null
  }
}
