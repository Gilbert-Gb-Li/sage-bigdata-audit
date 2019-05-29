package bigdata.audit.vo.fetch.es

case class EsGetFetchConfig(override val component: String,
                            esIndex: String,
                            esType: String,
                            esId: String,
                            column: String
                           )
  extends EsFetchConfig(component, "es-get")
