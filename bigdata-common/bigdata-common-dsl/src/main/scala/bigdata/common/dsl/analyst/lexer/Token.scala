package bigdata.common.dsl.analyst.lexer

case class Token(data: String,
                 startRow: Int,
                 startCol: Int,
                 stopRow: Int,
                 stopCol: Int
               ) {
  def isEmpty: Boolean = {
    if (data == null || data.isEmpty) {
      return true
    }
    false
  }

  def nonEmpty: Boolean = {
    if (data == null || data.isEmpty) {
      return false
    }
    true
  }

  override def toString: String = s"$data, $startRow:$startCol"
}
