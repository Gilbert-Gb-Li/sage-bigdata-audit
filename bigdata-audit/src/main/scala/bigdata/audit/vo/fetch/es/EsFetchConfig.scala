package bigdata.audit.vo.fetch.es

import bigdata.audit.vo.fetch.FetchConfig

abstract class EsFetchConfig(val component: String,
                             override val `type`: String)
  extends FetchConfig(`type`)
