package bigdata.audit.service.fetch.es

import java.util.{Calendar, Date}

import bigdata.audit.vo.fetch.es.EsGetFetchConfig
import org.scalatest.FunSuite

class EsFetchServiceTest extends FunSuite {

  test("es get") {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, -1)
    val date = calendar.getTime
    val column = "indicator_value"


    val esIndex = "(concat 'used_car_d_' (date-format (field 'timestamp') 'yyyyMM'))"
    val esType = "data"
    val esId = "(concat 'stats_' (date-format (field 'timestamp') 'yyyy-MM-dd') '_YX001')"
    val config = EsGetFetchConfig("es1", esIndex, esType, esId, column)
    val service = EsGetFetchService(config)
    val res = service.process(date)
    println(s"res: $res")
  }

}
