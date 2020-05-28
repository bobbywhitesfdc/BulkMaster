package bobby.sfdc.prototype.rest;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.gson.Gson;

import bobby.sfdc.prototype.rest.json.DataServicesVersionsResponse;


class DataServicesVersionsResponseTest {

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
	void testUnmarshallJSON()  {
		final String original = "[{\"label\":\"Winter '11\",\"url\":\"/services/data/v20.0\",\"version\":\"20.0\"},\n"
			+ "{\"label\":\"Spring '11\",\"url\":\"/services/data/v21.0\",\"version\":\"21.0\"},\n"
			+ "{\"label\":\"Summer '11\",\"url\":\"/services/data/v22.0\",\"version\":\"22.0\"},\n"
			+ "{\"label\":\"Winter '12\",\"url\":\"/services/data/v23.0\",\"version\":\"23.0\"},\n"
			+ "{\"label\":\"Spring '12\",\"url\":\"/services/data/v24.0\",\"version\":\"24.0\"},\n"
			+ "{\"label\":\"Summer '12\",\"url\":\"/services/data/v25.0\",\"version\":\"25.0\"},\n"
			+ "{\"label\":\"Winter '13\",\"url\":\"/services/data/v26.0\",\"version\":\"26.0\"},\n"
			+ "{\"label\":\"Spring '13\",\"url\":\"/services/data/v27.0\",\"version\":\"27.0\"},\n"
			+ "{\"label\":\"Summer '13\",\"url\":\"/services/data/v28.0\",\"version\":\"28.0\"},\n"
			+ "{\"label\":\"Winter '14\",\"url\":\"/services/data/v29.0\",\"version\":\"29.0\"},\n"
			+ "{\"label\":\"Spring '14\",\"url\":\"/services/data/v30.0\",\"version\":\"30.0\"},\n"
			+ "{\"label\":\"Summer '14\",\"url\":\"/services/data/v31.0\",\"version\":\"31.0\"},\n"
			+ "{\"label\":\"Winter '15\",\"url\":\"/services/data/v32.0\",\"version\":\"32.0\"},\n"
			+ "{\"label\":\"Spring '15\",\"url\":\"/services/data/v33.0\",\"version\":\"33.0\"},\n"
			+ "{\"label\":\"Summer '15\",\"url\":\"/services/data/v34.0\",\"version\":\"34.0\"},\n"
			+ "{\"label\":\"Winter '16\",\"url\":\"/services/data/v35.0\",\"version\":\"35.0\"},\n"
			+ "{\"label\":\"Spring '16\",\"url\":\"/services/data/v36.0\",\"version\":\"36.0\"},\n"
			+ "{\"label\":\"Summer '16\",\"url\":\"/services/data/v37.0\",\"version\":\"37.0\"},\n"
			+ "{\"label\":\"Winter '17\",\"url\":\"/services/data/v38.0\",\"version\":\"38.0\"},\n"
			+ "{\"label\":\"Spring '17\",\"url\":\"/services/data/v39.0\",\"version\":\"39.0\"},\n"
			+ "{\"label\":\"Summer '17\",\"url\":\"/services/data/v40.0\",\"version\":\"40.0\"},\n"
			+ "{\"label\":\"Winter '18\",\"url\":\"/services/data/v41.0\",\"version\":\"41.0\"},\n"
			+ "{\"label\":\"Spring ’18\",\"url\":\"/services/data/v42.0\",\"version\":\"42.0\"},\n"
			+ "{\"label\":\"Summer '18\",\"url\":\"/services/data/v43.0\",\"version\":\"43.0\"},\n"
			+ "{\"label\":\"Winter '19\",\"url\":\"/services/data/v44.0\",\"version\":\"44.0\"},\n"
			+ "{\"label\":\"Spring '19\",\"url\":\"/services/data/v45.0\",\"version\":\"45.0\"},\n"
			+ "{\"label\":\"Summer '19\",\"url\":\"/services/data/v46.0\",\"version\":\"46.0\"},\n"
			+ "{\"label\":\"Winter '20\",\"url\":\"/services/data/v47.0\",\"version\":\"47.0\"},\n"
			+ "{\"label\":\"Spring '20\",\"url\":\"/services/data/v48.0\",\"version\":\"48.0\"}]";
		
	   // Parse from JSON to response object
		DataServicesVersionsResponse[] response =  new Gson().fromJson(original, DataServicesVersionsResponse[].class);

		DataServicesVersionsResponse last = response[response.length-1];
		
		// Check a few key fields
		assertEquals("Spring '20",last.label);
		assertEquals("/services/data/v48.0",last.url);
		assertEquals("48.0",last.version);
	}

}

