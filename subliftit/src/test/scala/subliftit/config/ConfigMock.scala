package subliftit.config

import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import no.java.submitit.config.{Keys, ConfigKey}
import no.java.submitit.config.Keys.{submititBaseUrl, eventName}

trait ConfigMock extends Config {
  private var lookupFunc: PartialFunction[ConfigKey, Option[String]] = {case _ => throw new MatchError("No match function set up")}

  def configSetup(func: PartialFunction[ConfigKey, Option[String]]) {
    lookupFunc = func
  }

  override def configValue(key: ConfigKey): Option[String] = lookupFunc(key)

}

@RunWith(classOf[JUnitRunner])
class ConfigMockTest extends FunSuite {

   test("That configmock works as expected and check what the mock api looks like") {

     intercept[Exception] {
       new Config{
         configValue(eventName)
       }
     }

     intercept[MatchError] {
       val config = new Config with ConfigMock {
         configValue(submititBaseUrl)
       }
     }

     intercept[MatchError] {
       val config = new Config with ConfigMock {
         configSetup{case `eventName` => None}
         configValue(submititBaseUrl)
       }
     }

     val res = Some("an event")
     val myClass = new Config with ConfigMock {
       configSetup{case `eventName` => res}
       assert(res === configValue(eventName))
     }
   }

}