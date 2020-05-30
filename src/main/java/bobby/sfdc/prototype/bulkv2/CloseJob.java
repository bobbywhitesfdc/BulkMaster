package bobby.sfdc.prototype.bulkv2;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.bulkv2.json.CloseJobRequest;
import bobby.sfdc.prototype.bulkv2.json.JobInfo;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class CloseJob extends AbstractAPIBase {
	public CloseJob(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	public static final String RESOURCE="/services/data/v%apiversion%/jobs/ingest/%jobId%";
	public JobInfo execute(final String jobId, final String subCommand) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + getURLFromURLTemplate(getVersionedResource(RESOURCE),"jobId",jobId));

		HttpPatch patchJob = new HttpPatch(builder.build());
		CloseJobRequest request = new CloseJobRequest();
		request.state= subCommand;
		
		patchJob.setEntity(new StringEntity(request.toJson()));
		patchJob.setHeader(HttpHeaders.ACCEPT,"application/json");
		patchJob.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		
		APIExecutor<JobInfo> api = new APIExecutor<JobInfo>(JobInfo.class,getAuthToken());
		return api.processAPIPatchResponse(client, patchJob);
	}
}