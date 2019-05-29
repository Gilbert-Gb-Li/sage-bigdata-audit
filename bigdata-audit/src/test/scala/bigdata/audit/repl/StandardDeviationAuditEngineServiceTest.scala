package bigdata.audit.repl

import org.scalatest.FunSuite


class StandardDeviationAuditEngineServiceTest extends FunSuite {
  test("xx") {
    import bigdata.common.json._
    import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
    import com.fasterxml.jackson.annotation.JsonInclude.Include
    import com.fasterxml.jackson.annotation.PropertyAccessor
    import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper, SerializationFeature}
    import com.fasterxml.jackson.module.scala.DefaultScalaModule
    import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper


    def newMapper(): ObjectMapper with ScalaObjectMapper = {
      val mapper = new ObjectMapper() with ScalaObjectMapper

      mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PUBLIC_ONLY)
      mapper.registerModule(DefaultScalaModule)
      mapper.setSerializationInclusion(Include.NON_ABSENT)
      mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
      mapper.configure(SerializationFeature.INDENT_OUTPUT, false)
      mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

      mapper.setDateFormat(new BigdataDateFormat())
      mapper
    }

    val jsonMapper: ObjectMapper with ScalaObjectMapper = newMapper()
    val json =
      """
        |{
        |      "id": "1",
        |      "name": "已售原始数据",
        |      "type": "std",
        |      "downBound": 3,
        |      "upBound": 3,
        |      "days": 7,
        |      "preData": {
        |        "type": "es-count",
        |        "component": "es1",
        |        "indices": [
        |          "(concat 'used_car_r_' (curDate-format (field 'timestamp') 'yyyyMMdd')"
        |        ],
        |        "query": {
        |          "match_phrase": {
        |            "meta_table_name.keyword": {
        |              "query": "car_had_sale"
        |            }
        |          }
        |        }
        |      }
        |    }
      """.stripMargin
    //    val config = jsonMapper.readValue(json, classOf[StandardDeviationAlarmConfig])
    //    val fetchService = FetchService(config.auditInfos)
    //
    //    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    //    val date: Date = sdf.parse("2019-04-15")
    //    val days = 7
    //
    //
    //    val esJson =
    //      """
    //        |{
    //        |  "name": "ElasticSearch 集群配置",
    //        |  "config": {
    //        |    "type": "es",
    //        |    "clusterName": "NX_ES",
    //        |    "hosts": [
    //        |      {
    //        |        "host": "mvp-hadoop40",
    //        |        "port": 9300
    //        |      },
    //        |      {
    //        |        "host": "mvp-hadoop41",
    //        |        "port": 9300
    //        |      },
    //        |      {
    //        |        "host": "mvp-hadoop42",
    //        |        "port": 9300
    //        |      }
    //        |    ]
    //        |  }
    //        |}
    //      """.stripMargin
    //    val component = jsonMapper.readValue(esJson, classOf[Component])
    //    val esConfig = component.config.asInstanceOf[EsComponentConfig]
    //    val client = bigdata.node.utils.EsUtil.createClient(esConfig.clusterName, esConfig.hosts)
    //
    //    val service = fetchService.asInstanceOf[EsCountFetchService]
    //    (0 to days).reverse.map(i => {
    //      println(i)
    //    })
    //
    //    def deal(): Unit = {
    //      val calendar = Calendar.getInstance()
    //      val data1 = (0 to days).reverse.map(i => {
    //        calendar.setTime(date)
    //        calendar.add(Calendar.DAY_OF_MONTH, -i)
    //        val dstDate = calendar.getTime
    //        service.handle(client, dstDate)
    //      })
    //
    //
    //      val last = data1.last
    //
    //
    //      var auditInfos = new ListBuffer[Double]
    //      for (i <- 0 until data1.length - 1) {
    //        auditInfos += data1(i)
    //      }
    //
    //      auditInfos.foreach(println)

    //      val variance  = VarianceUtils.variance(auditInfos)
    //      val std: Double = Math.sqrt(variance)
    //      val up: Double = std * config.upBound
    //      val down: Double = -std * config.downBound
    //      val bound: Double = Math.abs(last - auditInfos.last)
    //      val res = if (bound > down && bound < up) {
    //        true
    //      } else {
    //        false
    //      }
    //      StandardDeviationAlarmInfo(res, config, auditInfos, date, last, variance, std, bound)
    //    }
    //
    //    deal()
    //  }
  }

}
