/*
 * Copyright 2009 javaBin
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package no.java.submitit.ems

import model._

import org.scalatest._
import org.scalatest.matchers.ShouldMatchers._
import org.scalatest.junit.JUnitRunner
import org.junit.runner.RunWith
import xml.Node

@RunWith(classOf[JUnitRunner])
class EmsConverterTest extends FunSuite {
  
  val original = 
        <person>
			<uuid>theId</uuid>
			<url>bogusurl</url>
			<name>Alf Støyle</name>
			<description/>
			<language>no</language>
			<email-addresses>
			<email-address>b@a.no</email-address>
			<email-address>a@b.no</email-address>
			</email-addresses>
			<tags>
			<tag>1tag</tag>
			<tag>2tag</tag>
			</tags>
			</person>

  val converter = new EmsConverter

  private def getXmlNode(node: Node, name: String, order: Int) = (node \\ name)(order)
  private def getXmlNode(node: Node, name: String): Node = getXmlNode(node, name, 0)

  test("convert person") {
    val s = new Speaker
    s.name = "Fredrik"
    s.email = "firstEmail"

    val res = converter.toEmsPerson(s, original)

    assert(<url>bogusurl</url> === getXmlNode(res, "url"))
    assert(<tag>1tag</tag> === getXmlNode(res, "tag"))
    assert(<tag>2tag</tag> === getXmlNode(res, "tag", 1))
    assert(<email-address>firstEmail</email-address> === getXmlNode(res, "email-address"))
    assert(<email-address>b@a.no</email-address> === getXmlNode(res, "email-address", 1))
    assert(<email-address>a@b.no</email-address> === getXmlNode(res, "email-address", 2))
  }



  val session = <ns2:session xmlns:ns2="http://xmlns.java.no/ems/external/1">
	<uuid>06aaec74-0a6d-4745-9b24-db43d01</uuid>
	<url>http://127.0.0.1:8040/ems-server/1/events/b582a071-d4c2-4a48-ac66-812a5ef94c1b/sessions/06aaec74-db43d0189d0d/b582a071-d</url>
	<event-id>b582a071-d4c2-4a48-ac66-812a5ef94c1b</event-id>
	<title>Going wild</title>
	<state>Pending</state>
	<format>Presentation</format>
	<language>en</language>
	<level>Intermediate</level>
	<body>&#13;
* Bladiip &#13;
&#13;
Bladi</body>
	<tags>
		<tag/>
	</tags>
	<keywords>
		<keyword/>
	</keywords>
	<speakers>
		<name>Person 1</name>
		<person-uuid>d1a04765-2882-4c56-9821-9ff74e4788</person-uuid>
		<description>Desc 2</description>
	</speakers>
	<speakers>
		<name>Person 2</name>
		<person-uuid>d3818bda-eaa3-410b-b159-ef9aa31</person-uuid>
		<description>Desc2</description>
	</speakers>
	<published>false</published>
	<expected-audience>This session is geared towards engineering and technical management staff who are lead open source projects and are looking for ways to create, leverage and grow their community of open source contributors. Basic knowledge of open source practices and engineering management is required.</expected-audience>
</ns2:session>



  test("convert session") {
    val p = new Presentation
    p.format = PresentationFormat.LightningTalk
    p.summary="Summary"
    p.outline = "theOutline"
    p.abstr = "abstract<level>lw</level>"
    p.equipment = "equi"
    p.language = Language.Norwegian
    p.title = "the title"
    
    val res = converter.toEmsSession(p, session)

    assert(<format>Quickie</format> === getXmlNode(res, "format"))
    assert(<body>{p.abstr}</body> === getXmlNode(res, "body"))

    //assert(<lead>{p.summary}</lead> === getXmlNode(res, "lead"))
    assert(<expected-audience>{p.expectedAudience}</expected-audience> === getXmlNode(res, "expected-audience"))
    // TODO what when element is not present... assert(<expected-audience>{p.equipment}</expected-audience> === getXmlNode(res, "expected-audience"))
    assert(<language>no</language> === getXmlNode(res, "language"))
    assert(<title>{p.title}</title> === getXmlNode(res, "title"))

  }



  /*
class EmsConverterTest extends TestCase("EMS converter") {
  val converter = new EmsConverter
  
  val speaker1 = Speaker("Alf Kristian Støyle", "aks@knowit.no", "Developer at KnowIT", None)
  val speaker2 = Speaker("Jon Anders Teigen", "mail@jteigen.com", "Developer at JPro", None)
  val speaker3 = Speaker("Fredrik Vraalsen", "fredrik@vraalsen.no", "Developer at KnowIT", None)
  val speakers = speaker1 :: speaker2 :: speaker3 :: Nil
  val presentation = Presentation("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Cras pellentesque.", 
                                      speakers, 
                                                                            """Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
Ut tortor quam, semper in, aliquet ac, posuere ac, leo.
Phasellus justo lectus, interdum eu, molestie vel, rutrum vel,
enim.""",
                                      """Lorem ipsum dolor sit amet, consectetuer adipiscing elit.
Ut tortor quam, semper in, aliquet ac, posuere ac, leo.
Phasellus justo lectus, interdum eu, molestie vel, rutrum vel,
enim.  Pellentesque ultrices magna et erat.  Integer velit urna,
aliquam sed, sodales quis, semper a, sem.  Proin convallis metus
sit amet sapien.  Nunc massa elit, pretium sed, interdum id,
placerat vel, tellus.  Duis ullamcorper nulla. Mauris eget tortor
in enim vestibulum ultrices.

Sed sapien diam, iaculis non, ullamcorper sagittis, rhoncus ac,
eros. Donec semper, sapien a consequat lobortis, nisl arcu
elementum metus, ac dapibus sem neque sed diam. Morbi eget
nisl. Class aptent taciti sociosqu ad litora torquent per conubia
nostra, per inceptos himenaeos. In hac habitasse platea
dictumst. Vestibulum rhoncus semper justo.""",
                                      """* Lorem
* ipsum
** dolor
** sit
* amet""", 
                                      Language.English, 
                                      Level.Intermediate, 
                                      PresentationFormat.LightningTalk,
                                      "equipment", 
                                      "expected audience")

  speaker1.personId = "id1"
  speaker2.personId = "id2"
  speaker3.personId = "id3"
  
  def testConvertSpeaker() {
    def emsSpeaker = converter toEmsSpeaker speaker1
    def result = converter fromEmsSpeaker emsSpeaker

    assertEquals(speaker1.personId, result.personId)
    assertEquals(speaker1.name, result.name)
//    assertEquals(speaker1.email, result.email)
    assertEquals(speaker1.bio, result.bio)
    assertEquals(speaker1.picture, result.picture)
    
    assertEquals(speaker1, result)
  }
  
  def testConvertPresentation() {
    val session = new Session()
    converter.updateSession(presentation, session)
    val result = converter.toPresentation(session)
    
    assertEquals(presentation.sessionId, result.sessionId)
    assertEquals(presentation.title, result.title)
    assertEquals(presentation.summary, result.summary)
    assertEquals(presentation.abstr, result.abstr)
    assertEquals(presentation.speakers, result.speakers)
    assertEquals(presentation.outline, result.outline)
    assertEquals(presentation.language, result.language)
    assertEquals(presentation.level, result.level)
    assertEquals(presentation.format, result.format)
    assertEquals(presentation.duration, result.duration)
    assertEquals(presentation.equipment, result.equipment)
    assertEquals(presentation.expectedAudience, result.expectedAudience)
  }
  */
}
