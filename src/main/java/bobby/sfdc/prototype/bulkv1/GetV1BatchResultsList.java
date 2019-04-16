package bobby.sfdc.prototype.bulkv1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.bulkv1.json.BulkV1BatchResultList;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

/**
 * Bulk API V1  List the Results for a particular Batch in a V1 Job
 * @author bobby.white
 *
 */
public class GetV1BatchResultsList extends AbstractAPIBase {
	public static final String RESOURCE="/services/async/45.0/job/%jobId%/batch/%batchId%/result";
	private static final Logger _logger = Logger.getLogger(GetV1BatchResultsList.class.getName());

	public GetV1BatchResultsList(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	public BulkV1BatchResultList execute(String jobId, String batchId) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {

	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    try {
	    	// Substitute both the JobID and BatchId
		    URIBuilder builder = new URIBuilder(getInstanceUrl() + getURLFromURLTemplate(getURLFromURLTemplate(RESOURCE,"batchId",batchId),"jobId",jobId));

	    	HttpGet getter = new HttpGet(builder.build());
	    	
	    	getter.setHeader(BULKV1_SESSION_HEADER,getAuthToken()); // V1 Jobs Expect this header
	    	getter.setHeader(HttpHeaders.ACCEPT,APPLICATION_JSON);
	    	getter.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);

		
	    	APIExecutor<BulkV1BatchResultList> api = new APIExecutor<BulkV1BatchResultList>(BulkV1BatchResultList.class,getAuthToken());
	    	BulkV1BatchResultList result = api.processAPIGetResponse(client, getter);
	    	_logger.info("BatchList: "+result);
	    	return result;
	    } finally {
	    	client.close();
	    }
	}


}
