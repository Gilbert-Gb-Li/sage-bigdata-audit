package bigdata.common.frame.entity

import bigdata.common.frame.enums.DataType

case class Field(id: String,
                 name: String,
                 tips: String,
                 `type`: DataType.DataType) {

}
