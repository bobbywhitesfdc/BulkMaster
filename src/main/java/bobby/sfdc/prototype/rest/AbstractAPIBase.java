package bobby.sfdc.prototype.rest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Logger;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import bobby.sfdc.prototype.BulkMaster;

public abstract class AbstractAPIBase {
	private static final Logger _logger = Logger.getLogger(AbstractAPIBase.class.getName());


	protected static final String BULKV1_SESSION_HEADER = "X-SFDC-Session";
	public static final String TEXT_CSV = "text/csv; charset=UTF-8";
	protected static final String APPLICATION_JSON = "application/json";
	protected final String authToken;
	protected final String instanceUrl;

	public AbstractAPIBase(String instanceUrl, String authToken) {
		this.instanceUrl=instanceUrl;
		this.authToken=authToken;
	}

	protected final String getAuthToken() {
		return this.authToken;
	}

	protected final String getInstanceUrl() {
		return this.instanceUrl;
	}
	
	/**
	 * Get an API VERSION specific instance of this Resource
	 * /services/data/v{%apiversion%}/jobs/ingest
	 * 
	 * @param resource Templated Resource URI /services/data/v{%apiversion%}/jobs/ingest
	 * @return Version specific resource URI - /services/data/v48.0/jobs/ingest
	 */
	protected String getVersionedResource(String resource) {
		return BulkMaster.getURLFromURLTemplate(resource,"apiversion",BulkMaster.getAPIVersion());
	}

	
	protected final String getURLFromURLTemplate(final String resource, final String paramName, final String paramValue) {
		return BulkMaster.getURLFromURLTemplate(resource,paramName,paramValue);
	}

	/**
	 * 
	 * @param getter
	 * @param fileName
	 * @return
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws FileNotFoundException
	 */
	protected String downloadFile(HttpGet getter, String fileName)
			throws URISyntaxException, IOException, ClientProtocolException, FileNotFoundException {
				// Simplistic implementation that uses a Synchronous approach and is memory intensive
			    CloseableHttpClient client = HttpClientBuilder.create().build();
			
			    APIExecutor.setAuthenticationHeader(getter,getAuthToken());
			    HttpResponse response = client.execute(getter);
			    
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
			
			
				    _logger.info("Saving file: " + fileName);
				    FileOutputStream fos = new FileOutputStream(fileName);
				    entity.writeTo(fos);
				    fos.close();
				    return fileName;
				}
				
				return null;
			}

}