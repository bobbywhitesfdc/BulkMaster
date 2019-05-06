/**
* JWTHelper
 * Bolt Content Solutions, LLC 2019
 */
package bobby.sfdc.prototype.oauth;


import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.*; 
import java.security.*;
import java.security.cert.CertificateException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;  

public class JWTHelper {
	/**
	 * 
	 */
	private static final Logger _logger = Logger.getLogger(JWTHelper.class.getName());
	private static final String SERVICES_OAUTH2_TOKEN_ENDPOINT = "/services/oauth2/token";

	public static final String SIGNING_ALGORITHM = "SHA256withRSA";
	public static final String CLAIMTEMPLATE = "'{'\"iss\": \"{0}\", \"sub\": \"{1}\", \"aud\": \"{2}\", \"exp\": \"{3}\"'}'";
    public static final String JWT_HEADER = "{\"alg\":\"RS256\"}";
    public static final String ENCODING = "UTF-8";
    private String clientId="3MVG9xOCXq4ID1uEoFCQ52NHSYe9ISyBnIorVdBQI0_y1p1qzXzvAo8G8Hzb9u5C00zXCJD6hUO5HOtAYRUpk";
    private String userName="bobby.white@de.boltsolutions.net";
    private String loginUrl="https://login.salesforce.com";
    private String keyStorePath="/Users/bobbywhitenc/privateKey.store";
    private String keyStorePassword="Backfl1p";
    private String certAlias="jwtkey";
    private String pkPassword="Backfl1p";

  public static void main(String[] args) {
	  
	  JWTHelper jwt = new JWTHelper();

 
	    try {
	      final StringBuffer token = new StringBuffer();
	
	      //Encode the JWT Header and add it to our string to sign
	      token.append(Base64.encodeBase64URLSafeString(JWT_HEADER.getBytes(ENCODING)));
	
	      //Separate with a period
	      token.append(".");
	
	      String payload = createClaim(jwt.getClientId(),jwt.getUserName(),jwt.getLoginUrl());
	
	      //Add the encoded claims object
	      token.append(Base64.encodeBase64URLSafeString(payload.getBytes(ENCODING)));
	
	      final PrivateKey privateKey = getPrivateKey(jwt.getKeyStorePath(), jwt.getKeyStorePassword(), jwt.getCertAlias(), jwt.getPkPassword());
	
	      final String signedPayload = signPayload(token.toString(), privateKey);
	
	      //Separate with a period
	      token.append(".");
	
	      //Add the encoded signature
	      token.append(signedPayload);
	
	      System.out.println(token.toString());
	      
		  CloseableHttpClient client = HttpClientBuilder.create().build();
		  try {
				// Set the 
				String baseUrl = jwt.getLoginUrl() + SERVICES_OAUTH2_TOKEN_ENDPOINT;
				// Send a post request to the OAuth URL.
				HttpPost oauthPost = new HttpPost(baseUrl);
				// The request body must contain these 5 values.
				List<BasicNameValuePair> parametersBody = new ArrayList<BasicNameValuePair>();
				parametersBody.add(new BasicNameValuePair("grant_type","urn:ietf:params:oauth:grant-type:jwt-bearer"));
				parametersBody.add(new BasicNameValuePair("assertion", token.toString()));
				oauthPost.setEntity(new UrlEncodedFormEntity(parametersBody, ENCODING));

				// Execute the request.
				_logger.info("POST " + baseUrl + "...\n");
				HttpResponse response = client.execute(oauthPost);
				int code = response.getStatusLine().getStatusCode();
				
				_logger.info("Code="+code);
				
				String authTokenJson = EntityUtils.toString(response.getEntity());
				
				_logger.info(authTokenJson);

			  
		  } finally {
			  client.close();
		  }
	
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
  }


/**
 * @param token
 * @param privateKey
 * @return
 * @throws NoSuchAlgorithmException
 * @throws InvalidKeyException
 * @throws SignatureException
 * @throws UnsupportedEncodingException
 */
protected static String signPayload(final String payload, PrivateKey privateKey)
		throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
	//Sign the JWT Header + "." + JWT Claims Object
      Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
      signature.initSign(privateKey);
      signature.update(payload.getBytes(ENCODING));
      String signedPayload = Base64.encodeBase64URLSafeString(signature.sign());
	return signedPayload;
}


/**
 * Get the Private Key from the Keystore
 * 
 * @param keyStorePath
 * @param keyStorePassword
 * @param certAlias
 * @param pkPassword
 * @return
 * @throws KeyStoreException
 * @throws IOException
 * @throws NoSuchAlgorithmException
 * @throws CertificateException
 * @throws FileNotFoundException
 * @throws UnrecoverableKeyException
 */
protected static PrivateKey getPrivateKey(final String keyStorePath
										, final String keyStorePassword
										, final String certAlias
										, final String pkPassword) throws KeyStoreException, IOException, NoSuchAlgorithmException,
		CertificateException, FileNotFoundException, UnrecoverableKeyException {
	  //Load the private key from a keystore
      KeyStore keystore = KeyStore.getInstance("JKS");
      keystore.load(new FileInputStream(keyStorePath), keyStorePassword.toCharArray());
      PrivateKey privateKey = (PrivateKey) keystore.getKey(certAlias, pkPassword.toCharArray());
	return privateKey;
}


	/**
	 * @return A JSON formatted claim
	 */
	protected static String createClaim(final String clientId
			, final String userName
			, final String loginUrl) {
		
		//Create the JWT Claims Object
	      String[] claimArray = new String[4];
	      claimArray[0] = clientId;
	      claimArray[1] = userName;
	      claimArray[2] = loginUrl;
	      claimArray[3] = Long.toString( ( System.currentTimeMillis()/1000 ) + 300);
	      MessageFormat claims;
	      claims = new MessageFormat(CLAIMTEMPLATE);
	      String payload = claims.format(claimArray);
		return payload;
	}


	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}


	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}


	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}


	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}


	/**
	 * @return the loginUrl
	 */
	public String getLoginUrl() {
		return loginUrl;
	}


	/**
	 * @param loginUrl the loginUrl to set
	 */
	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}


	/**
	 * @return the keyStorePassword
	 */
	public String getKeyStorePassword() {
		return keyStorePassword;
	}


	/**
	 * @param keyStorePassword the keyStorePassword to set
	 */
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}


	/**
	 * @return the certAlias
	 */
	public String getCertAlias() {
		return certAlias;
	}


	/**
	 * @param certAlias the certAlias to set
	 */
	public void setCertAlias(String certAlias) {
		this.certAlias = certAlias;
	}


	/**
	 * @return the pkPassword
	 */
	public String getPkPassword() {
		return pkPassword;
	}


	/**
	 * @param pkPassword the pkPassword to set
	 */
	public void setPkPassword(String pkPassword) {
		this.pkPassword = pkPassword;
	}


	/**
	 * @return the keyStorePath
	 */
	public String getKeyStorePath() {
		return keyStorePath;
	}


	/**
	 * @param keyStorePath the keyStorePath to set
	 */
	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}
}
