package bigdata.audit.vo.fetch.es

case class EsColumnFetchConfig(column: String,
                               sortField: String,
                               sortType: String,
                               override val component: String,
                               indices: Seq[String],
                               query: Map[String, Any])
  extends EsFetchConfig(component, "es-count")
