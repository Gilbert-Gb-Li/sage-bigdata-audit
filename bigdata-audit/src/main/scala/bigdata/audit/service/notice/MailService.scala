package bigdata.audit.service.notice

import bigdata.audit.dao.ComponentDAO
import bigdata.audit.vo.notice.MailConfig
import bigdata.common.frame.vo.component.{ComponentConfig, SmtpComponentConfig}
import bigdata.common.json.JsonMapper
import bigdata.common.log4j2.Logging
import com.sun.mail.util.MailSSLSocketFactory
import javax.mail.internet.MimeMessage
import javax.mail.{Authenticator, PasswordAuthentication, Session, Transport}

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.util.Try

case class MailService(config: MailConfig) extends NoticeService with JsonMapper with Logging {
  val dao = ComponentDAO()

  def process(title: String, content: String): Boolean = {
    val properties = System.getProperties
    val component = Await.result(dao.getById(config.component), Duration.Inf) match {
      case Some(x) =>
        x
      case _ =>
        throw new Exception("mail component is empty")
    }
    val componentConfig = jsonMapper.readValue(component.data, classOf[ComponentConfig]) match {
      case x: SmtpComponentConfig =>
        x
      case _ =>
        throw new Exception("mail component type is error")
    }


    // 设置邮件服务器
    properties.setProperty("mail.smtp.host", componentConfig.host)
    // ssl
    if (componentConfig.ssl) {
      val sf = new MailSSLSocketFactory()
      sf.setTrustAllHosts(true)
      properties.put("mail.smtp.ssl.enable", "true")
      properties.put("mail.smtp.ssl.socketFactory", sf)
    }
    // auth
    val session = if (componentConfig.auth) {
      properties.put("mail.smtp.auth", "true")
      Session.getDefaultInstance(properties, PwdAuthenticator(componentConfig.user, componentConfig.password))
    } else {
      Session.getDefaultInstance(properties)
    }
    val message = new MimeMessage(session)
    import javax.mail.Message
    import javax.mail.internet.InternetAddress
    // Set From: 头部头字段
    message.setFrom(new InternetAddress(componentConfig.user))

    // Set To: 头部头字段
    if (config.to != null && config.to.nonEmpty) {
      config.to.foreach(to => {
        Try {
          message.addRecipient(Message.RecipientType.TO, new InternetAddress(to))
        }
      })
    }

    if (config.cc != null && config.cc.nonEmpty) {
      config.cc.foreach(cc => {
        Try {
          message.addRecipient(Message.RecipientType.CC, new InternetAddress(cc))
        }
      })
    }
    if (config.bcc != null && config.bcc.nonEmpty) {
      config.bcc.foreach(bcc => {
        Try {
          message.addRecipient(Message.RecipientType.BCC, new InternetAddress(bcc))
        }
      })
    }

    // Set Subject: 头部头字段
    message.setSubject(title)
    message.setContent(content, "text/html;charset=utf-8")
    Transport.send(message)
    if (logger.isDebugEnabled) {
      logger.debug(s"alarm content:$content")
    }
    logger.info("send mail ok")
    true
  }

  case class PwdAuthenticator(userName: String, password: String) extends Authenticator {
    override def getPasswordAuthentication: PasswordAuthentication = {
      //发件人邮件用户名、密码
      new PasswordAuthentication(userName, password)
    }
  }

}
