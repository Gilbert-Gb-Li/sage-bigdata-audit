package bigdata.audit.vo.notice

/**
  *
  * @param component smtp服务器地址
  * @param to        收件人
  * @param cc        抄送
  * @param bcc       密送
  */
case class Ding2Config(url: String,
                       atMobiles: Seq[String] = Nil,
                       isAtAll: Boolean = false)
  extends NoticeConfig("ding2")
