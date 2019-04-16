package bobby.sfdc.prototype.bulkv2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class GetJobResults extends AbstractAPIBase {
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
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + getURLFromURLTemplate(RESOURCE,"jobId",jobId) + kind.getURI());
	    
	    String fileName = outputDir + File.separator + jobId + kind.getSuffix();
		HttpGet getter = new HttpGet(builder.build());
	    
	    return downloadFile(getter, fileName);
		
	}

}
