package bigdata.common.core

import com.typesafe.config.{Config, ConfigFactory}

import scala.util.Try

object Constants {
  val TIME_FORMAT_DEFAULT: String = "yyyy-MM-dd HH:mm:ss.S Z"
  val TIME_FORMAT_DEFAULT_SIZE: Int = TIME_FORMAT_DEFAULT.length

  val TIME_FORMAT_END_SECOND: String = "yyyy-MM-dd HH:mm:ss"
  val TIME_FORMAT_END_SECOND_SIZE: Int = TIME_FORMAT_END_SECOND.length

  val TIME_FORMAT_END_DAY: String = "yyyy-MM-dd"
  val TIME_FORMAT_END_DAY_SIZE: Int = TIME_FORMAT_END_DAY.length

  val TIME_SIZE_JAVA: Int = System.currentTimeMillis.toString.length
  val TIME_SIZE_UNIX: Int = TIME_SIZE_JAVA - 3
}

class Constants(val resourceBasename: String) {
  protected implicit val config: Config = ConfigFactory.load(resourceBasename)

  def getString(key: String, default: String = null): String = Try {
    var tmp = config.getString(key)
    if (tmp == null) {
      return default
    }
    tmp = tmp.trim
    if (tmp.length == 0) {
      return default
    }
    tmp
  }.getOrElse(default)

  def getDouble(key: String, default: Double): Double = Try {
    val tmp = config.getInt(key)
    if (tmp.isNaN) {
      return default
    }
    tmp.toDouble
  }.getOrElse(default)

  def getInt(key: String, default: Int): Int = Try {
    val tmp = config.getInt(key)
    if (tmp.isNaN) {
      return default
    }
    tmp
  }.getOrElse(default)

  def getConfig(key: String, default: Config = null): Config = Try {
    val tmp = config.getConfig(key)
    if (tmp.isEmpty) {
      return default
    }
    tmp
  }.getOrElse(default)

  def getBoolean(key: String, default: Boolean): Boolean = Try {
    config.getBoolean(key)
  }.getOrElse(default)
}
