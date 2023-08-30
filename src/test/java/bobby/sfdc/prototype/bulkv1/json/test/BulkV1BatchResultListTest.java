package bobby.sfdc.prototype.bulkv1.json.test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bobby.sfdc.prototype.bulkv1.json.BulkV1BatchResultList;

class BulkV1BatchResultListTest extends BulkV1BatchResultList {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testUnmarshallXML() throws JAXBException {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
				"<result-list xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\">\n" + 
				"  <result>752x00000004CJE</result>\n" +
				"  <result>752x00000005CJE</result>\n" +
				"</result-list>";
		
		JAXBContext jaxbContext = JAXBContext.newInstance(BulkV1BatchResultList.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xml);
		BulkV1BatchResultList result = (BulkV1BatchResultList) unmarshaller.unmarshal(reader);
		
		assertEquals(2,result.results.size());
		assertEquals("752x00000004CJE",result.results.get(0));
		assertEquals("752x00000005CJE",result.results.get(1));

	}

}
