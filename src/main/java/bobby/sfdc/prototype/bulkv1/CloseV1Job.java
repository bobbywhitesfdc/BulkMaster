package bobby.sfdc.prototype.bulkv1;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.bulkv1.json.CloseV1JobRequest;
import bobby.sfdc.prototype.bulkv1.json.BulkV1JobResponse;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class CloseV1Job extends AbstractAPIBase {
	public static final String RESOURCE = "/services/async/45.0/job/%jobId%";
	private static final Logger _logger = Logger.getLogger(CloseV1Job.class.getName());

	public CloseV1Job(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	public BulkV1JobResponse execute(String jobId) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {

	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    try {
		    URIBuilder builder = new URIBuilder(getInstanceUrl() + getURLFromURLTemplate(RESOURCE,"jobId",jobId));

	    	HttpPost post = new HttpPost(builder.build());
	    	
	    	CloseV1JobRequest request=new CloseV1JobRequest();
	    	request.state="Closed";
		
	    	post.setEntity(new StringEntity(request.toJson()));
	    	post.setHeader(BULKV1_SESSION_HEADER,getAuthToken()); // V1 Jobs Expect this header
	    	post.setHeader(HttpHeaders.ACCEPT,APPLICATION_JSON);
	    	post.setHeader(HttpHeaders.CONTENT_TYPE, APPLICATION_JSON);

		
	    	APIExecutor<BulkV1JobResponse> api = new APIExecutor<BulkV1JobResponse>(BulkV1JobResponse.class,getAuthToken());
	    	BulkV1JobResponse result = api.processAPIPostResponse(client, post);
	    	_logger.info("JobId="+result.id);
	    	return result;
	    } finally {
	    	client.close();
	    }
	}


}
