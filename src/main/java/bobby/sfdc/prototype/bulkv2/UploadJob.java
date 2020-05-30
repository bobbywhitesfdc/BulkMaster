package bobby.sfdc.prototype.bulkv2;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.HttpHeaders;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.bulkv2.json.*;
import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class UploadJob extends AbstractAPIBase {
	public static final String METHOD="PUT";
	
	public UploadJob(String instanceUrl,String authToken) {
		super(instanceUrl,authToken);
	}
	public void execute(String contentUrl,String inputFileName) throws ClientProtocolException, IOException, AuthenticationException, URISyntaxException {
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + "/" + contentUrl);

		HttpPut put = new HttpPut(builder.build());
		put.setHeader(HttpHeaders.CONTENT_TYPE, "text/csv");
		
		File inputFile = new File(inputFileName);
		if (!inputFile.exists()) {
			throw new IllegalArgumentException("Invalid input filename: " + inputFileName);
		}
		FileInputStream inputStream = new FileInputStream(inputFile);
		EntityBuilder b = EntityBuilder.create();
		b.setStream(inputStream);
		put.setEntity(b.build());
		
		APIExecutor<FileUploadResponse> api = new APIExecutor<FileUploadResponse>(FileUploadResponse.class,getAuthToken());
		api.processAPIPostResponse(client, put);		// create the request body
	}
}