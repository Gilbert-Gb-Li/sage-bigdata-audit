package bigdata.audit.vo.fetch

import bigdata.audit.vo.fetch.es.{EsColumnFetchConfig, EsCountFetchConfig, EsGetFetchConfig}
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import com.fasterxml.jackson.annotation.JsonTypeInfo.{As, Id}
import com.fasterxml.jackson.annotation.{JsonSubTypes, JsonTypeInfo}

@JsonTypeInfo(use = Id.NAME, include = As.EXISTING_PROPERTY, property = "type")
@JsonSubTypes(Array(
  new Type(value = classOf[EsCountFetchConfig], name = "es-count"),
  new Type(value = classOf[EsColumnFetchConfig], name = "es-column"),
  new Type(value = classOf[EsGetFetchConfig], name = "es-get"),
  new Type(value = classOf[EsCountFetchConfig], name = "")
))
abstract class FetchConfig(val `type`: String) {

}
