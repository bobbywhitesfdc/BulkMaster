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

import bobby.sfdc.prototype.bulkv1.json.BulkV1BatchInfo;

class BulkV1BatchInfoTest {

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
		final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><batchInfo\n" + 
				"   xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\">\n" + 
				" <id>7512M00000YA6k6QAD</id>\n" + 
				" <jobId>7502M00000Sp7doQAB</jobId>\n" + 
				" <state>Queued</state>\n" + 
				" <createdDate>2019-04-15T18:08:00.000Z</createdDate>\n" + 
				" <systemModstamp>2019-04-15T18:08:00.000Z</systemModstamp>\n" + 
				" <numberRecordsProcessed>0</numberRecordsProcessed>\n" + 
				" <numberRecordsFailed>0</numberRecordsFailed>\n" + 
				" <totalProcessingTime>0</totalProcessingTime>\n" + 
				" <apiActiveProcessingTime>0</apiActiveProcessingTime>\n" + 
				" <apexProcessingTime>0</apexProcessingTime>\n" + 
				"</batchInfo>";
		JAXBContext jaxbContext = JAXBContext.newInstance(BulkV1BatchInfo.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xml);
		BulkV1BatchInfo result = (BulkV1BatchInfo) unmarshaller.unmarshal(reader);
		
		// Check a few key fields
		assertEquals("7512M00000YA6k6QAD",result.id);
		assertEquals("7502M00000Sp7doQAB",result.jobId);
		assertEquals("Queued",result.state);
	}

}
