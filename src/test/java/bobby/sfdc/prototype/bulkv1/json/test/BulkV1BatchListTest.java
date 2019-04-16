package bobby.sfdc.prototype.bulkv1.json.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bobby.sfdc.prototype.bulkv1.json.BulkV1BatchList;

class BulkV1BatchListTest {

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
	    final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><batchInfoList\n" + 
	    		"   xmlns=\"http://www.force.com/2009/06/asyncapi/dataload\">\n" + 
	    		" <batchInfo>\n" + 
	    		"  <id>751D00000004YjwIAE</id>\n" + 
	    		"  <jobId>750D00000004T5OIAU</jobId>\n" + 
	    		"  <state>NotProcessed</state>\n" + 
	    		"  <createdDate>2016-01-10T00:59:47.000Z</createdDate>\n" + 
	    		"  <systemModstamp>2016-01-10T01:00:19.000Z</systemModstamp>\n" + 
	    		"  <numberRecordsProcessed>0</numberRecordsProcessed>\n" + 
	    		"  <numberRecordsFailed>0</numberRecordsFailed>\n" + 
	    		"  <totalProcessingTime>0</totalProcessingTime>\n" + 
	    		"  <apiActiveProcessingTime>0</apiActiveProcessingTime>\n" + 
	    		"  <apexProcessingTime>0</apexProcessingTime>\n" + 
	    		" </batchInfo>\n" + 
	    		" <batchInfo>\n" + 
	    		"  <id>751D00000004Yk1IAE</id>\n" + 
	    		"  <jobId>750D00000004T5OIAU</jobId>\n" + 
	    		"  <state>Completed</state>\n" + 
	    		"  <createdDate>2016-01-10T00:59:47.000Z</createdDate>\n" + 
	    		"  <systemModstamp>2016-01-10T01:00:19.000Z</systemModstamp>\n" + 
	    		"  <numberRecordsProcessed>100000</numberRecordsProcessed>\n" + 
	    		"  <numberRecordsFailed>0</numberRecordsFailed>\n" + 
	    		"  <totalProcessingTime>1000</totalProcessingTime>\n" + 
	    		"  <apiActiveProcessingTime>1000</apiActiveProcessingTime>\n" + 
	    		"  <apexProcessingTime>0</apexProcessingTime>\n" + 
	    		" </batchInfo>\n" + 
	    		" <batchInfo>\n" + 
	    		"  <id>751D00000004Yk2IAE</id>\n" + 
	    		"  <jobId>750D00000004T5OIAU</jobId>\n" + 
	    		"  <state>Completed</state>\n" + 
	    		"  <createdDate>2016-01-10T00:59:47.000Z</createdDate>\n" + 
	    		"  <systemModstamp>2016-01-10T01:00:19.000Z</systemModstamp>\n" + 
	    		"  <numberRecordsProcessed>100000</numberRecordsProcessed>\n" + 
	    		"  <numberRecordsFailed>0</numberRecordsFailed>\n" + 
	    		"  <totalProcessingTime>1000</totalProcessingTime>\n" + 
	    		"  <apiActiveProcessingTime>1000</apiActiveProcessingTime>\n" + 
	    		"  <apexProcessingTime>0</apexProcessingTime>\n" + 
	    		" </batchInfo>\n" + 
	    		" <batchInfo>\n" + 
	    		"  <id>751D00000004Yk6IAE</id>\n" + 
	    		"  <jobId>750D00000004T5OIAU</jobId>\n" + 
	    		"  <state>Completed</state>\n" + 
	    		"  <createdDate>2016-01-10T00:59:47.000Z</createdDate>\n" + 
	    		"  <systemModstamp>2016-01-10T01:00:19.000Z</systemModstamp>\n" + 
	    		"  <numberRecordsProcessed>100000</numberRecordsProcessed>\n" + 
	    		"  <numberRecordsFailed>0</numberRecordsFailed>\n" + 
	    		"  <totalProcessingTime>1000</totalProcessingTime>\n" + 
	    		"  <apiActiveProcessingTime>1000</apiActiveProcessingTime>\n" + 
	    		"  <apexProcessingTime>0</apexProcessingTime>\n" + 
	    		" </batchInfo>\n" + 
	    		" <batchInfo>\n" + 
	    		"  <id>751D00000004Yk7IAE</id>\n" + 
	    		"  <jobId>750D00000004T5OIAU</jobId>\n" + 
	    		"  <state>Completed</state>\n" + 
	    		"  <createdDate>2016-01-10T00:59:47.000Z</createdDate>\n" + 
	    		"  <systemModstamp>2016-01-10T01:00:19.000Z</systemModstamp>\n" + 
	    		"  <numberRecordsProcessed>50000</numberRecordsProcessed>\n" + 
	    		"  <numberRecordsFailed>0</numberRecordsFailed>\n" + 
	    		"  <totalProcessingTime>500</totalProcessingTime>\n" + 
	    		"  <apiActiveProcessingTime>500</apiActiveProcessingTime>\n" + 
	    		"  <apexProcessingTime>0</apexProcessingTime>\n" + 
	    		" </batchInfo>\n" + 
	    		"</batchInfoList>";
	    
		JAXBContext jaxbContext = JAXBContext.newInstance(BulkV1BatchList.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xml);
		BulkV1BatchList result = (BulkV1BatchList) unmarshaller.unmarshal(reader);
		
		assertEquals(5,result.batches.size());
	}

}
