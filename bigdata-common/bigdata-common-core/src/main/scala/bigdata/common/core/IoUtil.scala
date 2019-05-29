package bigdata.common.core

import java.io._

import scala.io.Source
import scala.util.Try

object IoUtil {
  def load(str: String, encoding: String = "UTF-8"): String = {
    val in = getResource(str)
    Source.fromInputStream(in, encoding).mkString("\n")
  }


  def getResource(str: String): InputStream = {
    Thread.currentThread().getContextClassLoader.getResourceAsStream(str)
  }

  def copy(in: InputStream, out: FileOutputStream): Unit = {
    val buffer = new Array[Byte](1024)
    var ch = 0
    var flag = true
    while (flag) {
      ch = in.read(buffer)
      flag = ch != -1
      if (flag) {
        out.write(buffer, 0, ch)
      }
    }
    close(in, out)
  }

  def write(out: File, content: String, encoding: String = "UTF-8"): Unit = {
    Try {
      out.getParentFile.mkdirs()
    }
    val writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(out), encoding))
    writer.write(content)
    writer.close()
  }

  def close(clients: Closeable*): Unit = {
    if (clients != null && clients.nonEmpty) {
      clients.foreach(client => {
        Try {
          client.close()
        }
      })
    }
  }


  def read(str: String, encoding: String = "UTF-8"): String = {
    val in = getClass.getClassLoader.getResourceAsStream(str)
    val source = Source.fromInputStream(in, encoding)
    source.mkString
  }

  def read(str: File, encoding: String): String = Try {
    val source = Source.fromFile(str, encoding)
    source.mkString
  }.getOrElse(null)


  def readIgnoreFirst(str: String, encoding: String = "UTF-8", ignoreFirst: Boolean = true): String = {
    val in = new FileInputStream(str)
    val source = Source.fromInputStream(in, encoding)
    if (ignoreFirst) {
      val iterator = source.getLines()
      iterator.next()
      iterator.mkString("\n")
    } else {
      source.mkString
    }
  }
}
