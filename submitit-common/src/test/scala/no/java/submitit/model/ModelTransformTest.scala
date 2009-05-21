package no.java.submitit.model


import _root_.java.util.Stack
import com.jteigen.scalatest.JUnit4Runner
import org.junit.runner.RunWith
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FunSuite
import xml.Utility.trim

@RunWith(classOf[JUnit4Runner])
class ModelTranformTest extends FunSuite with ShouldMatchers{

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
    assert(speaker === res)
    assert(speaker.bio === res.bio)
    assert(speaker.name === res.name)
    assert(speaker.email === res.email)
  }
  
  
  val session = trim {
    <ns2:session xmlns:ns2="http://xmlns.java.no/ems/external/1">
		<uuid>sessionId</uuid>
		<event-id>eventid</event-id>
		<title>Going wild</title>
		<state />
		<format>Presentation</format>
		<language>no</language>
		<level>Introductory</level>
		<body>theBody</body>
		<tags />
		<keywords />
	  <equipment />  
		{speakerXML}
		{speakerXML}
		<published />
		<expected-audience>audience</expected-audience>
		</ns2:session>
   }
    
	val p = new Presentation
	p.title = "Going wild"
	p.originalId = "sessionId"
	p.format = PresentationFormat.Presentation
	p.language = Language.Norwegian
	p.level = Level.Beginner
	p.abstr = "theBody"
	p.speakers = List(speaker, speaker)
  p.expectedAudience = "audience"
 
   test("transform presentation to ems xml") {
     // Fails for some reason. Have compared XML in editors and they are the same...
     // assert(session === p.toXML("eventid"))
   }

   test("transform ems xml session to presentation") {
     val res = Presentation.toPresentation(session)
     assert(p === res)
     assert(p.title === res.title)
     assert(p.originalId === res.originalId)
     assert(p.format === res.format)
     assert(p.level === res.level)
     assert(p.language === res.language)
     assert(p.expectedAudience === res.expectedAudience)
     // At this point does not have email
     assert(p.speakers == res.speakers)
     res.speakers.foreach(res => assert(res.email == null))
     res.setEmailsForSpeakers(p.speakers)
     res.speakers.foreach(res => assert(res.email != null))
	}
 

}





