package bobby.sfdc.prototype.bulkv1;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class GetV1BatchResultContent extends AbstractAPIBase {
	public static final String RESOURCE="/services/async/45.0/job/%jobId%/batch/%batchId%/result/%resultId%";
	private static final Logger _logger = Logger.getLogger(GetV1BatchResultContent.class.getName());

	public GetV1BatchResultContent(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	
	/**
	 * Retrieve the results of a Bulk V1 Query batch results
	 * @param jobId
	 * @param batchId
	 * @param resultId
	 * @param outputDir
	 * @throws URISyntaxException
	 * @throws ClientProtocolException
	 * @throws IOException
	 * @throws AuthenticationException
	 */
	public void execute(String jobId, String batchId, String resultId, String outputDir) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {

	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    try {
	    	// Substitute both the JobID, BatchId, and ResultId with 3 nested calls
		    URIBuilder builder = new URIBuilder(getInstanceUrl() 
		    			  + getURLFromURLTemplate(
		    				getURLFromURLTemplate(
		    				getURLFromURLTemplate(RESOURCE,"batchId",batchId)
		    				,"jobId",jobId)
		    				,"resultId",resultId));

	    	HttpGet getter = new HttpGet(builder.build());
	    	getter.setHeader(BULKV1_SESSION_HEADER,getAuthToken()); // V1 Jobs Expect this header
	    	
	    	String fileName = outputDir + File.separator + jobId+'-'+batchId+'-'+resultId+ ".csv";
	    	
	    	downloadFile(getter, fileName);
	    	
	    	_logger.info("saved file: " + fileName);
	    	return;
	    } finally {
	    	client.close();
	    }

	}
	
	

}
