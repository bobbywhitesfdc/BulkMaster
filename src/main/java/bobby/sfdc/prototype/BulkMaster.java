/**
 * 
 */
package bobby.sfdc.prototype;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.oauth.AuthenticationHelper;
import bobby.sfdc.prototype.oauth.json.OAuthTokenSuccessResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bobby.sfdc.prototype.rest.APIExecutor;
import bobby.sfdc.prototype.bulkv2.*;
import bobby.sfdc.prototype.bulkv2.json.*;


/**
 * Commandline Interface to execute Bulk API 2.0 jobs
 * 
 * @author bobby.white
 *
 */
public class BulkMaster  {
	public static final String DEFAULT_LOGIN_URL = "https://login.salesforce.com";
	private static final String CONSUMER_KEY_PROP = "consumer.key";
	private static final String CONSUMER_SECRET_PROP = "consumer.secret";
	private static final String LOGIN_URL_PROP = "login.url";

	private Gson _gson;
	private String _authToken=null;
	private String _instanceUrl=null;
	private boolean _isInitialized=false;
	private String consumerKey=null;
	private String consumerSecret=null;
	private String loginUrl=null;
	private String _id;
	private String jobId;
	private String objectName;
	private static  Logger _logger = Logger.getLogger(BulkMaster.class.getName());
	
	public static final String DATE_INPUT_PATTERN="yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
	protected static DateTimeFormatter dateInputFormatter = DateTimeFormat.forPattern(DATE_INPUT_PATTERN);



	public static void main(String[] args) {
		BulkMaster mgr = new BulkMaster();
		try {
			
			if (args.length < 2) {
				printSyntaxStatement();			
				return;
			}
			
			String userId = args[0];
			String password = args[1];
			
			String loginUrl = args.length >=3 ? args[2] : DEFAULT_LOGIN_URL;
			if (!loginUrl.startsWith("https:")) {
				loginUrl = DEFAULT_LOGIN_URL;
			}
			
			String ccEmail = args.length >=4 ? args[3] : null;
			if (ccEmail != null && (!ccEmail.contains("@") || ccEmail.startsWith("-"))) {
				ccEmail = null;
			}
			
			mgr.setOptionsFromCommandlineFlags(args);
			
						
			mgr.initConnectedAppFromConfigFile("/connectedapp.properties");
			
			mgr.setLoginUrl(loginUrl);		
			mgr.getAuthToken(userId, password);

			System.out.println("Instance URL:" + mgr.getInstanceUrl());
			


			GetAllJobsResponse jobs = mgr.getJobs();
			System.out.println("Jobs" + jobs);
						
			
		} catch (Throwable t) {
			_logger.log(Level.SEVERE,t.getMessage());
		}
		
	}

	/**
	 * Get the Bulk API Jobs information
	 * 
	 * @throws Throwable 
	 */
	public GetAllJobsResponse getJobs() throws Throwable {
	    CloseableHttpClient client = HttpClientBuilder.create().build();
	    URIBuilder builder = new URIBuilder(getInstanceUrl() + GetAllJobs.RESOURCE);


		HttpGet getJobs = new HttpGet(builder.build());
		APIExecutor<GetAllJobsResponse> api = new APIExecutor<GetAllJobsResponse>(GetAllJobsResponse.class,getAuthToken());
		return api.processAPIResponse(client, getJobs);
		
	}


	/**
	 * 
	 */
	public static void printSyntaxStatement() {
		System.out.println("userid and password are required parameters!:\n  myuser@example.com mypassword");
		System.out.println("Syntax:  BulkMaster <username> <password> [loginURL] [ccEmailAddress] [FLAGS]");
		System.out.println("\nIf omitted, default Login URL=" + DEFAULT_LOGIN_URL);
		
		System.out.println("\n\nFlags:");
		
		for (Flags flag : Flags.values()) {
			System.out.println("-"+ flag.getLabel() + " " + flag.getDescription());
		}
	}
	
	/**
	 * Process the Commandline Flags to set parameters for the News Feed
	 * @param args
	 */
	public void setOptionsFromCommandlineFlags(String[] args) {
	
		for (String flag : args) {
			if (!flag.startsWith("-")) {
				// it's not a flag, skip it
				continue;
			} 
			String parts[] = flag.substring(1).split(":");
			String flagPart = parts.length >= 1 ? parts[0] : "";
			String valuePart = parts.length > 1 ? parts[1] : "";
			
			
			if (flagPart.compareToIgnoreCase(Flags.JOBID.getLabel())==0) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Invalid JobID!"+valuePart);
				} else {
					this.jobId = valuePart;	
				}
			}
			
			if (flagPart.compareToIgnoreCase(Flags.LIST.getLabel())==0) {
				// Command is List Jobs
			}
			
			if (flagPart.compareToIgnoreCase(Flags.STATUS.getLabel())==0) {
				// Command is Get Job Status
			}
			
			if (flagPart.compareToIgnoreCase(Flags.INSERT.getLabel())==0) {
				// Command is INSERT records
			}
			if (flagPart.compareToIgnoreCase(Flags.RESULTS.getLabel())==0) {
				// Command is download RESULTS
			}			
			if (flagPart.compareToIgnoreCase(Flags.OBJECTNAME.getLabel())==0) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing Objectname!");
				} else {
					this.objectName = valuePart;	
				}				
			}
			
		}
		return;
	}


	
	public enum Flags {
		LIST("l","List Jobs"),
		INSERT("i","Insert Records"),
		UPSERT("u","Upsert Records"),
		DELETE("d","Delete Records"),
		STATUS("s","Get Job Status"),
		RESULTS("r","Get Job results"),
		JOBID("j","Hex ID of the Job"), 
		INPUTFILE("f","Input filename"), 
		OUTPUTDIR("D","Output Directory"),
		EXTERNALID("x","External ID fieldname for Upsert"),
		OBJECTNAME("o","Object name for Insert, Update, or Delete");
		private String label;
		private String description;

		Flags(String label, String description) {
		this.label=label;
		this.description=description;
		}
		public String getLabel() {
			return label;
		}
		/**
		 * @return the description
		 */
		public String getDescription() {
			return description;
		}
	}

	
	/**
	 * Obtain the AuthToken using the Userid/Password flow
	 * 
	 * Assumes that the connection info has already been initialized
	 * 
	 * @param userName in the form  user1@company.com
	 * @param password you may be required to append your Security Token to your password
	 * @throws IllegalStateException
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws AuthenticationException 
	 */
	public void getAuthToken(String userName, String password) throws IllegalStateException, AuthenticationException, HttpException, IOException {
		if (this.loginUrl==null) {
			throw new IllegalStateException("loginUrl must be initialized!");
		}
		if (this.consumerKey==null) {
			throw new IllegalStateException("consumerKey must be initialized!");
		}
		if (this.consumerSecret==null) {
			throw new IllegalStateException("consumerSecret must be initialized!");
		}


		OAuthTokenSuccessResponse result = new AuthenticationHelper().getAuthTokenUsingPasswordFlow(this.loginUrl, userName, password, this.consumerKey, this.consumerSecret);
		setAuthToken(result);
	}
	
	/**
	 * Set the Internal Members from the OAuthTokenSuccessResponse
	 * 
	 * @param auth  initialized by an OAuth based flow
	 */
	public void setAuthToken(OAuthTokenSuccessResponse auth) {
		this.setIsInitialized(true);
		this._authToken = auth.getAccess_token();
		this._id = auth.getId();
		this._instanceUrl = auth.getInstance_url();
	}

	/**
	 * Initialize Connection Information from the Properties File
	 * @param fileName
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void initConnectedAppFromConfigFile(String fileName) throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(BulkMaster.class.getResourceAsStream(fileName));
		this.consumerKey = (String) props.getProperty(CONSUMER_KEY_PROP);
		this.consumerSecret = (String) props.getProperty(CONSUMER_SECRET_PROP);
		this.loginUrl = (String) props.getProperty(LOGIN_URL_PROP);
	}
	
	
	/**
	 * Default constructor
	 */
	public BulkMaster() {
		registerGsonDeserializers();
	}

		
	
	public String getUserID() {
		String userId="";
		
		userId = _id.substring(_id.lastIndexOf('/')+1);
		
		return userId;
	}

	/**
	 * 
	 * @param templateURL - URL must include %parameterName%
	 * @param parameterName  - this must be in the URL, will be replaced with value
	 * @param value - the value to be substituted
	 * @return
	 */
	private String getURLFromURLTemplate(
			String templateURL, String parameterName,
			String value) {
		
		if (templateURL == null) {
			throw new IllegalArgumentException("Template URL must not be null!");
		}
		if (parameterName == null) {
			throw new IllegalArgumentException("parameterName must not be null!");
		}
		if (value == null) {
			throw new IllegalArgumentException("value must not be null!");
		}
		
		String marker = "%" + parameterName + "%";
		if (!templateURL.contains(marker)) {
			throw new IllegalArgumentException("Template (" + templateURL + ") must contain " + marker);
		}
		
		return templateURL.replace(marker, value);
	}

	/*
	 * Convert a String to a Joda DateTime
	 * */
	private DateTime convertToDateTime(String stringDate) {
		if (stringDate==null) {
			return null;
		} else {
			return dateInputFormatter.parseDateTime(stringDate);
		}
	}

	

	private void registerGsonDeserializers() {
		GsonBuilder gsonBuilder = new GsonBuilder();
		gsonBuilder.setPrettyPrinting();
//		gsonBuilder.registerTypeAdapter(FeedElementPage.class, new FeedElementPageDeserializer());
		_gson = gsonBuilder.create();
	}

	protected Gson getGson() {
		return _gson;
	}

	protected String getAuthToken() {
		return _authToken;
	}

	protected void setAuthToken(String authToken) {
		this._authToken = authToken;
	}

	protected String getInstanceUrl() {
		return _instanceUrl;
	}

	protected void setInstanceUrl(String instanceUrl) {
		this._instanceUrl = instanceUrl;
	}

	protected boolean isInitialized() {
		return _isInitialized;
	}

	protected void setIsInitialized(boolean isInitialized) {
		this._isInitialized = isInitialized;
	}

	/**
	 * @return the consumerKey
	 */
	public String getConsumerKey() {
		return consumerKey;
	}

	/**
	 * @param consumerKey the consumerKey to set
	 */
	public void setConsumerKey(String consumerKey) {
		this.consumerKey = consumerKey;
	}

	/**
	 * @return the consumerSecret
	 */
	public String getConsumerSecret() {
		return consumerSecret;
	}

	/**
	 * @param consumerSecret the consumerSecret to set
	 */
	public void setConsumerSecret(String consumerSecret) {
		this.consumerSecret = consumerSecret;
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

}
