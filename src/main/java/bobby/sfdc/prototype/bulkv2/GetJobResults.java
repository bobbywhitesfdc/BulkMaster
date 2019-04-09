package bobby.sfdc.prototype.bulkv2;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.APIExecutor;

public class GetJobResults extends AbstractBulkJob {
	private static final Logger _logger = Logger.getLogger(GetJobResults.class.getName());

	public static final String RESOURCE = "/services/data/v45.0/jobs/ingest/%jobId%";
	
	public enum RESULTKIND {
		SUCCESS("-success.csv","/successfulResults/"),
		FAILED("-failed.csv","/failedResults/"),
		UNPROCESSED("-unprocessed.csv","/unprocessedrecords/");
		private RESULTKIND(final String suffix, final String uri ) {
			this.suffix = suffix;
			this.uri= uri;
		}
		private final String suffix;
		private final String uri;
		public String getSuffix() {
			return suffix;
		}
		public String getURI() {
			return uri;
		}
	}

	public GetJobResults(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	public String execute(final String jobId, RESULTKIND kind, String outputDir) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + getURLFromURLTemplate(RESOURCE,"jobId",jobId) + kind.getURI());
	    
	    
	    // Simplistic implementation that uses a Synchronous approach and is memory intensive
		HttpGet getResults = new HttpGet(builder.build());
	    APIExecutor.setAuthenticationHeader(getResults,getAuthToken());
	    HttpResponse response = client.execute(getResults);
	    
		int code = response.getStatusLine().getStatusCode();
		
		_logger.info(response.getStatusLine().toString());

	    
		HttpEntity entity = response.getEntity();
		
		if (code == 200 && entity != null) {
			/** List the headers
			for (Header current : response.getAllHeaders()) {
				String name = current.getName();
				String value = current.getValue();
				_logger.info("header:" + name + " : " + value);
			}
			**/

		    String fileName = outputDir + File.separator + jobId + kind.getSuffix();
		    _logger.info("Saving file: " + fileName);
		    FileOutputStream fos = new FileOutputStream(fileName);
		    entity.writeTo(fos);
		    fos.close();
		    return fileName;
		}
		
		return null;
		
	}

}
