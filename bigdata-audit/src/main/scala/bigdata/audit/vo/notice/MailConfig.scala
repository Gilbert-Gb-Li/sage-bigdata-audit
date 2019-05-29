package bigdata.audit.vo.notice

/**
  *
  * @param component smtp服务器地址
  * @param to        收件人
  * @param cc        抄送
  * @param bcc       密送
  */
case class MailConfig(component: String,
                      to: Seq[String],
                      cc: Seq[String] = null,
                      bcc: Seq[String] = null
                     )
  extends NoticeConfig("mail")
