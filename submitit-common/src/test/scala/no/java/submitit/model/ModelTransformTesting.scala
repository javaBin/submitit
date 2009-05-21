package no.java.submitit.model


import _root_.java.util.Stack
import com.jteigen.scalatest.JUnit4Runner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite

/**
 * Created by IntelliJ IDEA.
 * User: stoyle
 * Date: May 21, 2009
 * Time: 10:09:38 AM
 * To change this template use File | Settings | File Templates.
 */

@RunWith(classOf[JUnit4Runner])
class ModelTranformTesting extends FunSuite with ShouldMatchers{

  val speaker = new Speaker
  speaker.name = "Alf"
  speaker.email = "mail"
  speaker.bio = "bio"
  speaker.originalId = "theId"
  
  val personXML =
      <person>
			<uuid>theId</uuid>
			<url/>
			<name>Alf</name>
			<description>bio</description>
			<language>en</language>
			<email-addresses>
			<email-address>mail</email-address>
			</email-addresses>
			<tags />
			</person>
   
	val personWithMultipeEmailXML =
			<person>
			<uuid>theId</uuid>
			<url/>
			<name>Alf</name>
			<description>bio</description>
			<language>en</language>
			<email-addresses>
			<email-address>mail</email-address>
			<email-address>mail2</email-address>
			<email-address>mail3</email-address>
			</email-addresses>
			<tags />
			</person>
			
   val speakerXML =
      <speakers>
      <name>Alf</name>
      <person-uuid>theId</person-uuid>
      <description>bio</description>
      </speakers>

  test("Tranform speaker to person xml") {
    assert(personXML === speaker.toPersonXml(Language.English))
  }


  test("Tranform speaker to session speaker xml") {
    assert(speakerXML === speaker.toSessionSpeakerXML)
  }
  
  test("transform person xml to speaker") {
    val res = Speaker.fromPersonXML(personWithMultipeEmailXML)
    println(res)
    assert(speaker === res)
    assert(speaker.bio === res.bio)
    assert(speaker.name === res.name)
    assert(speaker.email === res.email)
  }

}





