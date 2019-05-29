package bigdata.audit.service

import bigdata.audit.service.notice.MailService
import bigdata.audit.vo.notice.MailConfig
import bigdata.common.json.JsonMapper
import org.scalatest.FunSuite

class MailServiceTest extends FunSuite with JsonMapper {
  test("config") {
    val pwd = "8N8ahQUii92RfpAK"
    val config = MailConfig("smtp", Seq("taoistwar@163.com"))
    println(jsonMapper.writerWithDefaultPrettyPrinter().writeValueAsString(config))
  }

  test("sto self") {
    // bigdata
    val pwd = "8N8ahQUii92RfpAK"
    val config = MailConfig("smtp", Seq("taoistwar@163.com"))
    val service = MailService(config)
//    val res = service.process("笔记2", "回灌text3")
//    assert(res === true)
  }

  test("cc") {
    // bigdata
    val pwd = "haiYU2015"
    val config = MailConfig("smtp", Seq("taoistwar@163.com"),
      Seq("bigdata_audit@163.com", "ningguanyi@haima.me"))
    val service = MailService(config)
//    val res = service.processHtml("笔记2", "回灌text3")
//    assert(res === true)
  }
}
