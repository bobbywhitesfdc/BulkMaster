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

import bobby.sfdc.prototype.bulkv1.json.CreateV1BatchResponse;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class CreateV1Batch extends AbstractAPIBase {
	public static final String RESOURCE = "/services/async/%apiversion%/job/%jobId%/batch";
	private static final Logger _logger = Logger.getLogger(CreateV1Batch.class.getName());

	public CreateV1Batch(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	
	public CreateV1BatchResponse execute(String jobId, String query) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {

	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    try {
		    URIBuilder builder = new URIBuilder(getInstanceUrl() + getURLFromURLTemplate(getVersionedResource(RESOURCE),"jobId",jobId));

	    	HttpPost post = new HttpPost(builder.build());
		
	    	post.setEntity(new StringEntity(query));
	    	post.setHeader("X-SFDC-Session",getAuthToken()); // V1 Jobs Expect this header
	    	post.setHeader(HttpHeaders.ACCEPT,"application/json");
	    	post.setHeader(HttpHeaders.CONTENT_TYPE, "text/csv; charset=UTF-8");

		
	    	APIExecutor<CreateV1BatchResponse> api = new APIExecutor<CreateV1BatchResponse>(CreateV1BatchResponse.class,getAuthToken());
	    	CreateV1BatchResponse result = api.processAPIPostResponse(client, post);
	    	_logger.info("BatchId="+result.id);
	    	return result;
	    } finally {
	    	client.close();
	    }
	}


}
