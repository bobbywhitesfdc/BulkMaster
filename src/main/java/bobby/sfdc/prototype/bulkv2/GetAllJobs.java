package bobby.sfdc.prototype.bulkv2;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.bulkv2.json.GetAllJobsResponse;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;

public class GetAllJobs extends AbstractBulkJob {
	public GetAllJobs(final String instanceUrl, final String authToken) {
		super(instanceUrl, authToken);
	}
	public static final String RESOURCE="/services/data/v45.0/jobs/ingest";
	public GetAllJobsResponse execute() throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + RESOURCE);


		HttpGet getJobs = new HttpGet(builder.build());
		APIExecutor<GetAllJobsResponse> api = new APIExecutor<GetAllJobsResponse>(GetAllJobsResponse.class,getAuthToken());
		return api.processAPIGetResponse(client, getJobs);
	}
}