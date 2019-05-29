package bigdata.audit.practise

import java.util.Date

import org.quartz._
import org.quartz.impl.StdSchedulerFactory

object QuartzTest {
  val scheduler = StdSchedulerFactory.getDefaultScheduler

  val job: JobDetail = JobBuilder.newJob(classOf[HelloQuartz]) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
    .withIdentity("job1", "group1") //定义name/group
    .usingJobData("name", "quartz") //定义属性
    .build()

  def main(args: Array[String]): Unit = {


    val trigger = buildTriger()

    //加入这个调度
    scheduler.scheduleJob(job, trigger)
    //启动之
    scheduler.start()

    //运行一段时间后关闭
    Thread.sleep(100000000)
    scheduler.shutdown(true)
  }

  private def buildTriger() = {
    val trigger = TriggerBuilder.newTrigger()
      .withIdentity(s"trigger1", "group1")
      .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?"))
      .build()
    trigger
  }

  class HelloQuartz extends Job {
    override def execute(context: JobExecutionContext): Unit = {
      val detail = context.getJobDetail()
      val name = detail.getJobDataMap().getString("name")
      System.out.println("say hello to " + name + " at " + new Date())
      reset()
    }
  }

  def reset() = {
    scheduler.deleteJob(new JobKey("job1", "group1"))
    val trigger = buildTriger()
    scheduler.scheduleJob(job, trigger)


  }

}
