package bobby.sfdc.prototype.rest;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.json.DataServicesVersionsResponse;

/**
 * Unauthenticated API to query the available API versions
 * @author bobby.white
 *
 */
public class DataServicesVersions extends AbstractAPIBase {
	public DataServicesVersions(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	public static final String RESOURCE="/services/data";
	public DataServicesVersionsResponse[] execute() throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + RESOURCE); // No Parameters required

		HttpGet getDataServices = new HttpGet(builder.build());
		
		getDataServices.setHeader(HttpHeaders.ACCEPT,"application/json");
		getDataServices.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");

		
		APIExecutor<DataServicesVersionsResponse[]> api = new APIExecutor<DataServicesVersionsResponse[]>(DataServicesVersionsResponse[].class,getAuthToken());
		return api.processAPIGetResponse(client, getDataServices);
	}
}

