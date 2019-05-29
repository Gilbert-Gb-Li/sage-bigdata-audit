package bigdata.audit.service.fetch

import java.util.Date

import bigdata.audit.service.fetch.es.{EsColumnFetchService, EsCountFetchService, EsGetFetchService}
import bigdata.audit.vo.fetch.FetchConfig
import bigdata.audit.vo.fetch.es.{EsColumnFetchConfig, EsCountFetchConfig, EsGetFetchConfig}

abstract class FetchService(val config: FetchConfig) {
  def process(date: Date): Double
}

object FetchService {
  def apply(config: FetchConfig): FetchService = {
    config match {
      case x: EsCountFetchConfig =>
        EsCountFetchService(x)
      case x: EsColumnFetchConfig =>
        EsColumnFetchService(x)
      case x: EsGetFetchConfig =>
        EsGetFetchService(x)
    }
  }
}