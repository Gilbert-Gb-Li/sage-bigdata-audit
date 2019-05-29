package bigdata.audit.service.notice

import java.util

import bigdata.audit.vo.notice.{Ding2Config, MailConfig, NoticeConfig}

trait NoticeService {
  def process(title: String, content: String): Boolean
}

object NoticeService {
  def apply(config: NoticeConfig): NoticeService = {
    config match {
      case x: MailConfig =>
        MailService(x)
      case x: Ding2Config =>
        Ding2Service(x)
    }
  }
}