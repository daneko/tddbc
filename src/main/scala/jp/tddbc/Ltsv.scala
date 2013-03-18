package jp.tddbc

class Ltsv {
  var keyList: List[String] = Nil
  var ltsvMap: Map[String, String] = Map()

  def set(key: String, value: String): Either[Throwable, Option[String]] = {
    (Option(key), Option(value)) match {
      case (None, _) | (Some(""), _) | (_, None) => return Left(new IllegalArgumentException)
      case _                                     =>
    }
    val ret = get(key)
    keyList = key :: keyList.filterNot(_ == key)
    ltsvMap = ltsvMap + (key -> value)
    Right(ret)
  }

  def get(key: String) = ltsvMap.get(key)

  def dump() = {
    val dumplist = for (
      key <- keyList;
      value <- get(key)
    ) yield key + ":" + value
    dumplist.reverse.reduce(_ + "\t" + _) + "\n"
  }
}
