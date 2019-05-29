package bigdata.common.log4j2

import org.apache.logging.log4j.{LogManager, Logger}


trait Logging {
  val logger: Logger = LogManager.getLogger(getClass)
}
