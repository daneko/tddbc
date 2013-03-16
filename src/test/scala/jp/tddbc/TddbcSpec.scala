package jp.tddbc

import org.specs2.mutable._
import org.specs2.specification.Scope
import org.specs2.runner.JUnitRunner
import org.junit.runner.RunWith

@RunWith(classOf[JUnitRunner])
class TddbcSpec extends Specification {
  sequential

  "The 'Hello world' string" should {
    "contain 11 characters" in context {
      "Hello world" must have size (11)
    }
    "start with 'Hello'" in new system {
      string must startWith("Hello")
    }
    "end with 'world'" in system2().e1
  }

  object context extends BeforeAfter {

    def before = ()

    def after = ()
  }

  trait system extends Scope {
    val string = "Hello world"
  }

  case class system2() {
    val string = "Hello world"

    def e1 = string must endWith("world")
  }

}
