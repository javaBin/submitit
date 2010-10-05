package subliftit.config

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import no.java.submitit.config.{Keys, ConfigKey}
import no.java.submitit.config.Keys.eventName

trait ConfigMock extends Config {
  def fromKey: PartialFunction[ConfigKey, Option[String]]

  override def configValue(key: ConfigKey): Option[String] = fromKey(key)

}

@RunWith(classOf[JUnitRunner])
class ConfigMockTest extends FunSuite {

   test("That configmock works as expected and check out the api") {
     val res = Some("an event")
     object myTestClass extends Config with ConfigMock {
       def fromKey = {case _ => res}
     }
     assert(res === myTestClass.configValue(eventName))
   }

}