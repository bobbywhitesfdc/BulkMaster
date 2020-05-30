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

import bobby.sfdc.prototype.bulkv1.json.CreateV1JobRequest;
import bobby.sfdc.prototype.bulkv1.json.BulkV1JobResponse;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class CreateV1Job extends AbstractAPIBase {
	public static final String RESOURCE = "/services/async/%apiversion%/job";
	private static final Logger _logger = Logger.getLogger(CreateV1Job.class.getName());


	public CreateV1Job(final String instanceUrl, final String authToken) {
		super(instanceUrl, authToken);
	}
	public BulkV1JobResponse execute(final String operation, final String objectName, boolean pkChunkingEnabled) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {
		

	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    try {
	    	URIBuilder builder = new URIBuilder(getInstanceUrl() + getVersionedResource(RESOURCE));

	    	HttpPost post = new HttpPost(builder.build());
	    	CreateV1JobRequest request = new CreateV1JobRequest();
	    	request.operation = operation;
	    	request.object = objectName;
	    	request.contentType="CSV";
	    	
	    	
			post.setEntity(new StringEntity(request.toJson()));
	    	post.setHeader("X-SFDC-Session",getAuthToken());
	    	if (pkChunkingEnabled) {
	    		post.setHeader("Sforce-Enable-PKChunking","true");
	    	}
	    	post.setHeader(HttpHeaders.ACCEPT,"application/json");
	    	post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8");

		
	    	APIExecutor<BulkV1JobResponse> api = new APIExecutor<BulkV1JobResponse>(BulkV1JobResponse.class,getAuthToken());
	    	BulkV1JobResponse result = api.processAPIPostResponse(client, post);
	    	_logger.info("JobId="+result.id);
	    	return result;
	    } finally {
	    	client.close();
	    }

	}

}
