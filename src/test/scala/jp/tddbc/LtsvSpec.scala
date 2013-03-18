package jp.tddbc

import org.specs2._

// format: OFF
class LtsvSpec extends Specification {
  def is =
  "TDDBC 2013-03-16 Tokyo".title ^
    """
LTSVを読み込むクラスを作る
今回 @akuraru さんとペアになって短時間で考えた結果
基本的にMapでKey/Valueを保持し
Keyの順番に関してはListで管理することにした
    """^                                         p^
    "LTSV一行を管理するクラス"                   ^
                                                 p^
    "key value をsetする場合"                    ^ set^
    "keyを指定して値を取得する場合"              ^ get^
    "dumpメソッドで出力する場合"                 ^ dump^
                                                 end

  def set =
    "key/valueを与えるとMapとListが更新される"                             ! TestSet().e1 ^
    "既に保有するキーの場合は、Mapのvalueが更新され、" +
      "Listで管理するkeyの順序は更新するキーが最後尾に来る"                ! TestSet().e2 ^
    "正常系ならEither::Right(None)が返る"                                  ! TestSet().e3 ^
    "正常系且つ更新ならEither::Right(Some(古いvalue))が返る"               ! TestSet().e4 ^
    "keyがnullの場合はLeft"                                                ! TestSet().e5 ^
    "keyが空文字はLeft"                                                    ! TestSet().e6 ^
    "valueがnullはLeft"                                                    ! TestSet().e7 ^
    "valueは空文字はOK(Right)"                                             ! TestSet().e8 ^
                                                                           endp

  def get =
    "keyが存在すればSome(value)が返る" ! TestGet().e1 ^
    "keyが存在しなければNoneが返る"    ! TestGet().e2 ^
                                       endp

  def dump =
    "dumpは tab区切り末尾改行のLTSVで出力する" ! TestDump().e1 ^
                                               endp

  // format: ON
  case class TestSet() {
    val ltsv: Ltsv = new Ltsv()
    def e1 = {
      ltsv.set("hoge", "huga")
      (ltsv.keyList === List("hoge")).orThrow
      ltsv.ltsvMap === Map("hoge" -> "huga")
    }

    def e2 = {
      ltsv.set("hoge", "huga")
      ltsv.set("bar", "fuga")
      (ltsv.keyList === List("bar", "hoge")).orThrow
      (ltsv.ltsvMap === Map("hoge" -> "huga", "bar" -> "fuga")).orThrow

      ltsv.set("hoge", "piyo")

      (ltsv.keyList === List("hoge", "bar")).orThrow
      (ltsv.ltsvMap === Map("hoge" -> "piyo", "bar" -> "fuga")).orThrow

      success
    }

    def e3 = {
      ltsv.set("hoge", "huga") match {
        case Right(n) => n must beNone
        case _        => ko
      }
    }

    def e4 = {
      ltsv.set("hoge", "huga")
      val r2 = ltsv.set("hoge", "foo")
      r2 match {
        case Right(Some(s)) => s === "huga"
        case _              => ko
      }
    }

    def e5 = {
      ltsv.set(null, "huga") must beLeft
    }

    def e6 = {
      ltsv.set("", "huga") must beLeft
    }

    def e7 = {
      ltsv.set("hoge", null) must beLeft
    }

    def e8 = {
      ltsv.set("hoge", "") must beRight
    }
  }

  case class TestGet() {
    val ltsv = new Ltsv()
    def e1 = {
      ltsv.set("hoge", "huga")
      ltsv.get("hoge") must beSome("huga")
    }
    def e2 = {
      ltsv.get("hoge") must beNone
    }
  }

  case class TestDump() {
    val ltsv = new Ltsv()
    def e1 = {
      ltsv.set("hoge", "huga")
      (ltsv.dump() === "hoge:huga\n").orThrow
      ltsv.set("bar", "fuga")
      (ltsv.dump() === "hoge:huga\tbar:fuga\n").orThrow
      ltsv.keyList = "piyo" :: ltsv.keyList
      ltsv.dump() === "hoge:huga\tbar:fuga\n"
    }
  }
}

