package bobby.sfdc.prototype.bulkv2;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.bulkv2.json.JobInfo;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class GetJobInfo extends AbstractAPIBase {
	public GetJobInfo(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	public static final String RESOURCE="/services/data/v%apiversion%/jobs/ingest/%jobId%";
	public JobInfo execute(final String jobId) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + getURLFromURLTemplate(getVersionedResource(RESOURCE),"jobId",jobId));

		HttpGet getInfo = new HttpGet(builder.build());
		APIExecutor<JobInfo> api = new APIExecutor<JobInfo>(JobInfo.class,getAuthToken());
		return api.processAPIGetResponse(client, getInfo);
	}
}