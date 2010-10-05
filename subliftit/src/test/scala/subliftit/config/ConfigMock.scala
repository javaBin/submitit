package subliftit.config

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import no.java.submitit.config.{Keys, ConfigKey}
import no.java.submitit.config.Keys.{submititBaseUrl, eventName}

trait ConfigMock extends Config {
  def setup: PartialFunction[ConfigKey, Option[String]]

  override def configValue(key: ConfigKey): Option[String] = setup(key)

}

@RunWith(classOf[JUnitRunner])
class ConfigMockTest extends FunSuite {

   test("That configmock works as expected and check what the mock api looks like") {

     intercept[Exception] {
       new Config{}.configValue(eventName)
     }

     intercept[MatchError] {
       val config = new Config with ConfigMock {
         def setup = {case `eventName` => None}
       }
       config.configValue(submititBaseUrl)
     }

     val res = Some("an event")
     object myTestClass extends Config with ConfigMock {
       def setup = {case `eventName` => res}
     }
     assert(res === myTestClass.configValue(eventName))
   }

}