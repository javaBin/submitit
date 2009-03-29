package no.java.submitit.ems

import junit.framework._
import Assert._
import model._


class EmsClientTest extends TestCase("EMS XML testing") {

  val xml = <ns2:events xmlns:ns2="http://xmlns.java.no/ems/external/1">
	<event>
		<id>1ca89aef-c7ce-45b5-95a0-630f55d7efa6
		</id>
		<name>JavaZone 2006</name>
		<tags />
		<rooms>
			<name>Scandinavia scene</name>
		</rooms>
		<rooms>
			<name>Stockholm</name>
		</rooms>
		<rooms>
			<name>K?benhavn</name>
		</rooms>
		<rooms>
			<name>Oslo</name>
		</rooms>
		<rooms>
			<name>Telemar</name>
		</rooms>
		<rooms>
			<name>Lofoten</name>
		</rooms>
		<rooms>
			<name>Brasseriet</name>
		</rooms>
		<rooms>
			<name>Helsingfors</name>
		</rooms>
		<rooms>
			<name>Helsingfors/K?benhavn</name>
		</rooms>
	</event>
	<event>
		<id>407f368f-41d4-4248-b825-39d0f973dfe1
		</id>
		<name>JavaZone 2007</name>
		<tags />
	</event>
	<event>
		<id>b582a071-d4c2-4a48-ac66-812a5ef94c1b</id>
		<name>JavaZone 2009</name>
		<tags />
	</event>
	<event>
		<id>5e130372-285d-49cd-aedb-7f306d97b04d
		</id>
		<name>Javazone 2008</name>
		<tags />
		<rooms>
			<name>Lab 1</name>
			<description></description>
		</rooms>
		<rooms>
			<name>Lab 2</name>
			<description></description>
		</rooms>
		<rooms>
			<name>Lab 3</name>
			<description></description>
		</rooms>
		<rooms>
			<name>Lab 4</name>
			<description></description>
		</rooms>
		<rooms>
			<name>Lab 5</name>
			<description></description>
		</rooms>
		<rooms>
			<name>Lab 6</name>
			<description></description>
		</rooms>
		<rooms>
			<name>BoF</name>
		</rooms>
	</event>
</ns2:events>
  
  
    
  def testGetSessionId() {
    val client = new EmsClient("JavaZone 2009", null, null, null)
    assertEquals("b582a071-d4c2-4a48-ac66-812a5ef94c1b", client.findEventInXML(xml))
    
  }
  
  
}
