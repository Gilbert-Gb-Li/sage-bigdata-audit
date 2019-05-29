package bigdata.audit.vo.notice

import bigdata.common.json.JsonMapper

abstract class NoticeConfig(val `type`: String) {

}

object NoticeConfig extends JsonMapper {
  def apply(`type`: String, data: String): NoticeConfig = {
    val mapper = newMapper()
    `type` match {
      case "mail" =>
        mapper.readValue(data, classOf[MailConfig])
      case "ding2" =>
        mapper.readValue(data, classOf[Ding2Config])
    }
  }
}
