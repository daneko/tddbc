package jp.tddbc

case class LTSV(var keyList: List[String] = Nil, var ltsvMap: Map[String, String] = Map()) {
  def set(key: String, value: String): Either[Any, Option[String]] = {
    keyList = key :: keyList.filterNot(_ == key)
    ltsvMap = ltsvMap + (key -> value)
    Right(None)
  }

  def get(key: String) = ltsvMap(key)

  def dump() = keyList.map(key => key + ":" + get(key)).reverse.reduce(_ + "\t" + _) + "\n"
}

