package bobby.sfdc.prototype.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVSplitManagerTest {

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
	void testGetExtension() {	
	    CSVSplitManager mgr = new CSVSplitManager();
	    assertEquals(".csv",mgr.getExtension("data.csv"));
	}
	
	@Test
	void testGetExtensionEmpty() {	
	    CSVSplitManager mgr = new CSVSplitManager();
	    assertEquals("",mgr.getExtension("data"));
	}
	
	@Test
	void testGetExtensionManyDots() {	
	    CSVSplitManager mgr = new CSVSplitManager();
	    assertEquals(".csv",mgr.getExtension("data.part.csv"));
	}

	@Test
	void testGetBase() {	
	    CSVSplitManager mgr = new CSVSplitManager();
	    assertEquals("data",mgr.getBase("data.csv"));
	}
	
	@Test
	void testGetBaseNoExt() {	
	    CSVSplitManager mgr = new CSVSplitManager();
	    assertEquals("data",mgr.getBase("data"));
	}

	@Test
	void testGetBaseManyDots() {	
	    CSVSplitManager mgr = new CSVSplitManager();
	    assertEquals("data.parts.extra",mgr.getBase("data.parts.extra.csv"));
	}
	@Test
	void testPartNumbering() {	
	    CSVSplitManager mgr = new CSVSplitManager();
	    mgr.setOriginal(new File("data.csv"));
	    mgr.setOutputDir(new File("."));
	    
	    assertEquals("data.0000.csv",mgr.getPartFile().getName());
	    assertEquals("data.0001.csv",mgr.getPartFile().getName());	}
}
