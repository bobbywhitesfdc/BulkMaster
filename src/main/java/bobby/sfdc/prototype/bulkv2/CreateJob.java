package bobby.sfdc.prototype.bulkv2;
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

import bobby.sfdc.prototype.bulkv2.json.*;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class CreateJob extends AbstractAPIBase {
	private static final Logger _logger = Logger.getLogger(CreateJob.class.getName());

	public CreateJob(String instanceUrl, String authToken) {
		super(instanceUrl,authToken);
	}
	public static final String RESOURCE="/services/data/v%apiversion%/jobs/ingest";
    public static final String METHOD="POST";
	public CreateJobResponse execute(final String objectName, final String operation, final String externalIdFieldName) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    try {
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + getVersionedResource(RESOURCE));

		HttpPost post = new HttpPost(builder.build());
		CreateJobRequest request = new CreateJobRequest();
		request.object = objectName;
		request.contentType="CSV";
		request.lineEnding="LF";
		request.columnDelimiter="COMMA";
		request.operation=operation;
		request.externalIdFieldName=externalIdFieldName;
		
		post.setEntity(new StringEntity(request.toJson()));
		post.setHeader(HttpHeaders.ACCEPT,"application/json");
		post.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		
		APIExecutor<CreateJobResponse> api = new APIExecutor<CreateJobResponse>(CreateJobResponse.class,getAuthToken());
		CreateJobResponse result = api.processAPIPostResponse(client, post);
		_logger.info("JobId="+result.id);
		return result;
	    } finally {
	    	client.close();
	    }
	}
}