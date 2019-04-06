package bobby.sfdc.prototype.rest;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.oauth.json.OAuthErrorResponse;

import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;

/**
 * Helper class that will execute REST API calls and unmarshal JSON 
 * results to Object form.
 * 
 * @author bobby.white
 *
 * @param <ResponseClassType>
 */
public class APIExecutor<ResponseClassType> {
	private static final Logger _logger = Logger.getLogger(APIExecutor.class.getName());
	
	private final Class<ResponseClassType> apiResponseClassType;

	private final String authToken;
	private String lastResponseBody = "";
	private int httpResultCode=0;
	
	/**
	 * Constructor which takes the expected Response Type Class so that it can instantiate them later
	 * @param clazz
	 */
	public APIExecutor(Class<ResponseClassType> clazz, String authToken) {
		this.apiResponseClassType= clazz;
		this.authToken=authToken;
	}
	
	/**
	 * @param client
	 * @param apiOperation The REST call to execute
	 * @param responseClass JavaClass which represents the expected JSON response from the API
	 * @return an instance of the responseClass or an exception
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws AuthenticationException
	 */
	public  ResponseClassType processAPIResponse(CloseableHttpClient client, HttpGet apiOperation) throws IOException,
			ClientProtocolException, AuthenticationException {
		String rawJsonResponse=null;
		try {
			apiOperation.addHeader("Authorization","Bearer "+ this.authToken);

			HttpResponse response = client.execute(apiOperation);
			int code = response.getStatusLine().getStatusCode();
			
			_logger.info(response.getStatusLine().toString());
			
			rawJsonResponse = EntityUtils.toString(response.getEntity());
			
			this.httpResultCode=code;
			this.setLastResponseBody(rawJsonResponse);

			
			if (code==200) {

				try {
					ResponseClassType r =  (ResponseClassType) new Gson().fromJson(rawJsonResponse, apiResponseClassType);
					return r;

				} catch (JsonSyntaxException | IllegalStateException e) {
					handleJsonSyntaxException(rawJsonResponse, e);
					return null;
				}
				

			} else {
				_logger.info(apiOperation.getURI().toString());
				// The call failed for some reason
				if (rawJsonResponse != null && rawJsonResponse.startsWith("{")) {
				    OAuthErrorResponse errorResponse = new Gson().fromJson(rawJsonResponse, OAuthErrorResponse.class);
				    throw new AuthenticationException(errorResponse);
				} else if (rawJsonResponse != null && rawJsonResponse.startsWith("[{")) {
				    throw new AuthenticationException(rawJsonResponse);
				} else {
				    throw new AuthenticationException(rawJsonResponse);
				}
			}
		} finally {		
			client.close();
		}
	}
	
	/**
	 * Convenience method to allow caller to reparse a result without repeating the server call
	 * 
	 * @param json a well formed json response
	 * @return an instance of the class
	 * @throws JsonSyntaxException
	 * @throws IllegalStateException
	 */
	public  ResponseClassType parseJsonBody(String json) throws JsonSyntaxException, IllegalStateException {
		if (json == null) throw new IllegalArgumentException("json parameter must not be null");
		ResponseClassType r =  (ResponseClassType) new Gson().fromJson(json, apiResponseClassType);
		return r;
	}
	
	/**
	 * 
	 * @param rawJsonResponse
	 * @param e an exception that occurred during parsing of the raw Json
	 */
	private void handleJsonSyntaxException(String rawJsonResponse,
			Throwable e) {
		String msg = e.getMessage();
		String extractExp = "\\d+$";
		
		if (msg.contains("line") && msg.contains("column")) {  // ends with 'at line xxx column yyyy'
			// if the message contains a location in the form:   line N column C   show the location in the original Json
			int index=0;
			Pattern p = Pattern.compile(extractExp);
			Matcher m = p.matcher(msg);

			if (m.find()) {
			   index=Integer.parseInt(m.group(0));
			   index = index >= 50 ? index-50 : index; // backup to show more context
			}
			_logger.log(Level.SEVERE,e.getMessage());
			_logger.log(Level.FINE,rawJsonResponse.substring(index));
			_logger.log(Level.FINER,rawJsonResponse);
			
		} else {
			_logger.log(Level.FINER,rawJsonResponse);
			_logger.log(Level.SEVERE,e.getMessage());
		}
		
		//saveToFile(JSON_ERRORFILE, rawJsonResponse);

	}

	/**
	 * @return the lastResponseBody
	 */
	public String getLastResponseBody() {
		return lastResponseBody;
	}

	/**
	 * @param lastResponseBody the lastResponseBody to set
	 */
	public void setLastResponseBody(String lastResponseBody) {
		this.lastResponseBody = lastResponseBody;
	}

	/**
	 * @return the httpResultCode
	 */
	public int getHttpResultCode() {
		return httpResultCode;
	}



}
