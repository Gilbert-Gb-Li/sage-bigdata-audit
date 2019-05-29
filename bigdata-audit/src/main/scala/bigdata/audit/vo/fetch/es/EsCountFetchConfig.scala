package bigdata.audit.vo.fetch.es

case class EsCountFetchConfig(override val component: String,
                              indices: Seq[String],
                              query: Map[String, Any])
  extends EsFetchConfig(component, "es-count")
