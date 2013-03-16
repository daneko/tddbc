package jp.tddbc

import org.specs2._
import org.junit.Test.None

// format: OFF
class TddbcAceptanceSpec extends Specification {
  def is = args(sequential = true) ^
  "LTSV"                 ^
                         p^
  "set"                  ^ set^
  "get"                  ^ get^
  "dump"                 ^ dump^end

  def set = p ^
    "MapとListが更新される" ! TestSet().e1 ^
    "同じキーの場合は、valueが更新され、Listが更新される" ! TestSet().e4 ^
    "正常系ならEither::Right(None)が返る" ! TestSet().e5 ^
    "正常系且つ更新ならEither::Right(Some)が返る" ! TestSet().e6 ^
    "keyがnullはだめ"                                                      ! pending ^
    "keyが空文字はだめ"                                                    ! pending ^
    "valueがnullはだめ"                                                    ! pending ^
    "valueは空文字でもOK"                                                  ! pending ^ endp

  def get = p ^
    "keyが存在すればvalueが取れる" ! e2 ^ endp

  def dump = p ^
    "dumpは{key:value}をタブつなぎで最後にエンターのStrinigを出力する" ! e3 ^ endp

  // format: ON
  case class TestSet(val ltsv: LTSV = LTSV()) {
    def e1 = {
      ltsv.set("hoge", "huga")
      (ltsv.keyList === List("hoge")).orThrow
      ltsv.ltsvMap === Map("hoge" -> "huga")
    }

    def e4 = {
      ltsv.set("hoge", "huga")
      ltsv.set("bar", "fuga")
      (ltsv.keyList === List("bar", "hoge")).orThrow
      (ltsv.ltsvMap === Map("hoge" -> "huga", "bar" -> "fuga")).orThrow

      ltsv.set("hoge", "piyo")

      (ltsv.keyList === List("hoge", "bar")).orThrow
      (ltsv.ltsvMap === Map("hoge" -> "piyo", "bar" -> "fuga")).orThrow

      success
    }

    def e5 = {
      ltsv.set("hoge", "huga") match {
        case Right(n) => n must beNone
        case _        => ko
      }
    }

    def e6 = {
      ltsv.set("hoge", "huga")
      val r2 = ltsv.set("hoge", "foo")
      r2 match {
        case Right(Some(s)) => s === "huga"
        case _              => ko
      }
    }
  }

  def e2 = {
    val ltsv = LTSV()
    ltsv.set("hoge", "huga")
    ltsv.get("hoge") === "huga"
  }
  def e3 = {
    val ltsv = LTSV()
    ltsv.set("hoge", "huga")
    (ltsv.dump() === "hoge:huga\n").orThrow
    ltsv.set("bar", "fuga")
    ltsv.dump() === "hoge:huga\tbar:fuga\n"
  }
}
