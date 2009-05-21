package no.java.submitit.model


import _root_.java.util.Stack
import com.jteigen.scalatest.JUnit4Runner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import xml.Utility.trim

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
  
  val personXML = trim {
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
   }
   
	val personWithMultipeEmailXML = trim {
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
   }
			
   val speakerXML = trim {
      <speakers>
      <name>Alf</name>
      <person-uuid>theId</person-uuid>
      <description>bio</description>
      </speakers>
   }

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
  
  
  val session = trim { 
    <ns2:session xmlns:ns2="http://xmlns.java.no/ems/external/1">
		<uuid>eventid</uuid>
		<event-id>sessionId</event-id>
		<title>Going wild</title>
		<state>Pending</state>
		<format>Presentation</format>
		<language>en</language>
		<level>Intermediate</level>
		<body></body>
		<tags />
		<keywords />
		speakerXML
		speakerXML
		<published />
		<expected-audience>This session is geared towards engineering and technical management staff who are lead open source projects and are looking for ways to create, leverage and grow their community of open source contributors. Basic knowledge of open source practices and engineering management is required.</expected-audience>
		</ns2:session>
  }
  
	val p = new Presentation
	p.title = "Going wild"
	p.originalId = "sessionId"
	p.title = "Going wild"
	p.title = "Going wild"
	p.title = "Going wild"
 
   test("transform presentation to ems xml") {
     // Not finished, still want to test
     assert(session != p.toXML("sessionId"))
   }
  
  
  

}





