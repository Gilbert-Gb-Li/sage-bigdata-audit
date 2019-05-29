package bigdata.audit.service.audit

import java.io.{ByteArrayOutputStream, File, OutputStreamWriter}
import java.util

import bigdata.audit.utils.Constants
import bigdata.audit.vo.audit.AuditInfo
import freemarker.template.{Configuration, DefaultObjectWrapper, Template}

object FtlAlarmContent {

  class FtlAlarmContent

  val cfg: Configuration = {
    val cfg = new Configuration(Configuration.VERSION_2_3_28)
    //    cfg.setClassForTemplateLoading(classOf[FtlAlarmContent], "../../../../")
    cfg.setDirectoryForTemplateLoading(new File(Constants.ftlPath))
    cfg.setObjectWrapper(new DefaultObjectWrapper(Configuration.VERSION_2_3_28))
    cfg
  }
  val curveTmplate: Template = cfg.getTemplate("/conf/audit/curve.ftl")
  val stdTemplate: Template = cfg.getTemplate("/conf/audit/std.ftl")
  val noticeMailTemplate: Template = cfg.getTemplate("/conf/audit/notice-mail.ftl")
  val noticeDing2Template: Template = cfg.getTemplate("/conf/audit/notice-ding2.ftl")
  val monitorMailTemplate: Template = cfg.getTemplate("/conf/audit/monitor-mail.ftl")
  val monitorDing2Template: Template = cfg.getTemplate("/conf/audit/monitor-ding2.ftl")

  def processStd(model: util.Map[String, Any]): String = {
    val temp = stdTemplate
    process(model, temp)
  }

  def processMonitorMail(model: util.Map[String, Any]): String = {
    val temp = monitorMailTemplate
    process(model, temp)
  }

  def processMonitorDing2(model: util.Map[String, Any]): String = {
    val temp = monitorDing2Template
    process(model, temp)
  }

  def processNoticeMail(model: util.Map[String, Any]): String = {
    val temp = noticeMailTemplate
    process(model, temp)
  }

  def processNoticeDing2(model: util.Map[String, Any]): String = {
    val temp = noticeDing2Template
    process(model, temp)
  }

  def processCurve(info: AuditInfo, model: util.Map[String, Any]): String = {
    val temp = curveTmplate
    process(model, temp)
  }

  private def process(model: util.Map[String, Any], temp: Template) = {
    val out = new ByteArrayOutputStream()
    val writer = new OutputStreamWriter(out)
    temp.process(model, writer)
    writer.flush()
    val dst = new String(out.toByteArray())
    dst
  }
}
