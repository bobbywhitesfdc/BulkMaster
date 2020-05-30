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

import bobby.sfdc.prototype.bulkv1.json.BulkV1BatchList;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class GetV1BatchInfo extends AbstractAPIBase {
	public static final String RESOURCE="/services/async/%apiversion%/job/%jobId%/batch";
	private static final Logger _logger = Logger.getLogger(GetV1BatchInfo.class.getName());


	public GetV1BatchInfo(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	
	public BulkV1BatchList execute(String jobId) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {

	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    try {
		    URIBuilder builder = new URIBuilder(getInstanceUrl() + getURLFromURLTemplate(getVersionedResource(RESOURCE),"jobId",jobId));

	    	HttpGet getter = new HttpGet(builder.build());
	    	
	    	getter.setHeader(BULKV1_SESSION_HEADER,getAuthToken()); // V1 Jobs Expect this header
	    	getter.setHeader(HttpHeaders.ACCEPT,APPLICATION_JSON);
	    	getter.setHeader(HttpHeaders.CONTENT_TYPE, TEXT_CSV);

		
	    	APIExecutor<BulkV1BatchList> api = new APIExecutor<BulkV1BatchList>(BulkV1BatchList.class,getAuthToken());
	    	BulkV1BatchList result = api.processAPIGetResponse(client, getter);
	    	_logger.info("BatchList: "+result);
	    	return result;
	    } finally {
	    	client.close();
	    }
	}


}
