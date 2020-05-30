package bobby.sfdc.prototype.rest;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.json.DataServicesVersionsResponse;

class DataServicesVersionsTest {

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
	void testExecute() {
		// Unauthenticated call to a Salesforce instance that won't change!
		final String instanceUrl = "https://org62.my.salesforce.com";
		DataServicesVersions api = new DataServicesVersions(instanceUrl,"");
		try {
			DataServicesVersionsResponse[] results = api.execute();
			assert(results.length > 0);
			
			DataServicesVersionsResponse latest = results[results.length-1];
			assertNotEquals(latest.label,null);
			assertNotEquals(latest.url,null);
			assertNotEquals(latest.version,null);
			
			Double versionValue = Double.valueOf(latest.version);
			assert(versionValue > 46.0 && versionValue < 200.0); // Being quite optimistic on the upper bound :-) 
			
		} catch (URISyntaxException | IOException | AuthenticationException e) {
			fail("Execution error.  Exception: " + e.getMessage()); 
		}
	}

}
