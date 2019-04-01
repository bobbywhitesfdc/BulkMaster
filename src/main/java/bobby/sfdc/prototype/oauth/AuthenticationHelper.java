package bobby.sfdc.prototype.oauth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import bobby.sfdc.prototype.oauth.json.OAuthErrorResponse;
import bobby.sfdc.prototype.oauth.json.OAuthTokenSuccessResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

public class AuthenticationHelper {
	private static final Logger _logger = Logger.getLogger(AuthenticationHelper.class.getName());
	private static final String SERVICES_OAUTH2_TOKEN_ENDPOINT = "/services/oauth2/token";

	private Gson _gson;
	public AuthenticationHelper() {
		this.registerGsonDeserializers();
	}
	public  OAuthTokenSuccessResponse getAuthTokenUsingPasswordFlow(String loginHost, String username,
	        String password, String clientId, String secret)
	        throws HttpException, IOException, AuthenticationException 
	{
	    // Set up an HTTP client that makes a connection to REST API.
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    try {


			// Set the SID.
			_logger.info("Logging in as " + username + " in environment " + loginHost);
			String baseUrl = loginHost + SERVICES_OAUTH2_TOKEN_ENDPOINT;
			// Send a post request to the OAuth URL.
			HttpPost oauthPost = new HttpPost(baseUrl);
			// The request body must contain these 5 values.
			List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
			parametersBody.add(new BasicNameValuePair("grant_type", "password"));
			parametersBody.add(new BasicNameValuePair("username", username));
			parametersBody.add(new BasicNameValuePair("password", password));
			parametersBody.add(new BasicNameValuePair("client_id", clientId));
			parametersBody.add(new BasicNameValuePair("client_secret", secret));
			oauthPost.setEntity(new UrlEncodedFormEntity(parametersBody, "utf-8"));

			// Execute the request.
			_logger.info("POST " + baseUrl + "...\n");
			HttpResponse response = client.execute(oauthPost);
			int code = response.getStatusLine().getStatusCode();
			
			_logger.info(response.getStatusLine().toString());
			
			if (code!=200) {
				// The call failed for some reason
				OAuthErrorResponse errorResponse = getGson().fromJson(EntityUtils.toString(response.getEntity()), OAuthErrorResponse.class);
				throw new AuthenticationException(errorResponse);
			}

			OAuthTokenSuccessResponse results = getGson().fromJson(EntityUtils.toString(response.getEntity()), OAuthTokenSuccessResponse.class);

			System.out.println(results.toString());
			
			return results;
			
			
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (AuthenticationException e) {
			// do nothing, let it past
			_logger.info(e.getMessage());
		} finally {
			if (client != null) {
				client.close();
			}
		}
		return null;
	    
	}
	
	/**
	 * This OAuth call is the 2nd step in the OAuth 2.0 Webserver flow.
	 * 
	 * 1. Web app redirects to Authorization Endpoint and user manually authorizes app, returning Code
	 * 2. This call takes Code and exchanges for an AuthToken and RefreshToken
	 * 3. AuthToken can be refreshed using RefreshToken
	 * 
	 * @param loginHost  for production this is https://login.salesforce.com
	 * @param redirectUrl  must match with the redirect URL configured in the connected App
	 * @param consumerKey  must match client_id of connected App
	 * @param consumerSecret must match client_secret of connected App
	 * @param code  returned from initial Authorize call in OAuth 2.0 Web App Flow
	 * @return SuccessResponse which contains AuthToken, InstanceUrl, and RefreshToken
	 * @throws AuthenticationException 
	 */
	public OAuthTokenSuccessResponse getAuthTokenFromCode(String loginHost, String redirectUrl, String consumerKey, String consumerSecret, String code) throws AuthenticationException {
	    // Set up an HTTP client that makes a connection to REST API.
	    CloseableHttpClient client = HttpClientBuilder.create().build();

		try {
			String baseUrl = loginHost + SERVICES_OAUTH2_TOKEN_ENDPOINT;
	        
	        HttpPost httpPost = new HttpPost(baseUrl);
	        List <NameValuePair> nvps = new ArrayList <NameValuePair>();
	        nvps.add(new BasicNameValuePair("grant_type", "authorization_code"));
	        nvps.add(new BasicNameValuePair("code", code));
	        nvps.add(new BasicNameValuePair("client_id", consumerKey));
	        nvps.add(new BasicNameValuePair("client_secret", consumerSecret));
	        nvps.add(new BasicNameValuePair("redirect_uri", redirectUrl));
	        
			try {
				httpPost.setEntity(new UrlEncodedFormEntity(nvps));
			} catch (UnsupportedEncodingException e) {
				_logger.warning(e.getMessage());
			}
	        
			HttpResponse response = client.execute(httpPost);
			int status = response.getStatusLine().getStatusCode();
			_logger.info("OAuth response status="+status);
			if (status!=200) {
				// The call failed for some reason
				OAuthErrorResponse errorResponse = getGson().fromJson(EntityUtils.toString(response.getEntity()), OAuthErrorResponse.class);
				throw new AuthenticationException(errorResponse);
			}

			OAuthTokenSuccessResponse results = getGson().fromJson(EntityUtils.toString(response.getEntity()), OAuthTokenSuccessResponse.class);
			return results;
			
		} catch (AuthenticationException e) {
			// do nothing, let it past
			_logger.info(e.getMessage());
			throw e;
		} catch (Throwable t) {
			_logger.log(Level.SEVERE,t.getMessage());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				_logger.warning(e.getMessage());
			}
		}
		
		return null;
	}


	
	private void registerGsonDeserializers() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
		_gson = gsonBuilder.create();
	}
	protected Gson getGson() {
		return _gson;
	}


}
