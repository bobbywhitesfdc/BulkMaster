/**
 * 
 */
package bobby.sfdc.prototype;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;

import bobby.sfdc.prototype.oauth.AuthenticationException;
import bobby.sfdc.prototype.oauth.AuthenticationHelper;
import bobby.sfdc.prototype.oauth.json.OAuthTokenSuccessResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bobby.sfdc.prototype.bulkv1.CloseV1Job;
import bobby.sfdc.prototype.bulkv1.CreateV1Batch;
import bobby.sfdc.prototype.bulkv1.CreateV1Job;
import bobby.sfdc.prototype.bulkv1.json.CreateV1BatchResponse;
import bobby.sfdc.prototype.bulkv1.json.BulkV1JobResponse;
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
	private Commands currentCommand=Commands.LIST; // Default Command
	private String jobId=null;
	private String objectName=null;
	private String externalIdFieldName=null;
	private String inputFileName="";
	private String outputDir="."; // Default to "here"
	private int pollingInterval=0;
	private String queryString;
	private static  Logger _logger = Logger.getLogger(BulkMaster.class.getName());
	
	public static final String DATE_INPUT_PATTERN="yyyy-MM-dd'T'HH:mm:ss.SSSZZ";

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
			
			mgr.executeCommand();
			
		} catch (Throwable t) {
			_logger.log(Level.SEVERE,t.getMessage());
		}
		
	}

	private void executeCommand() throws Throwable {
		
		switch(this.currentCommand) {
		case LIST:
			GetAllJobsResponse jobs = listJobsCommand();
			System.out.println("Jobs" + jobs);
			break;
		case INSERT:
			{
				CreateJobResponse result = createJobCommand(objectName,"insert","");
				uploadFileOperation(result.contentUrl,inputFileName);
				closeJobCommand(result.id,CloseJobRequest.UPLOADCOMPLETE);
				pollForResults(result.id);				
			}
			break;
		case UPSERT:
			{
				CreateJobResponse result = createJobCommand(objectName,"upsert",this.externalIdFieldName);
				uploadFileOperation(result.contentUrl,inputFileName);
				closeJobCommand(result.id,CloseJobRequest.UPLOADCOMPLETE);
				pollForResults(result.id);
			}
			break;
		case DELETE:
			{
				CreateJobResponse result = createJobCommand(objectName,"hardDelete","");
				uploadFileOperation(result.contentUrl,inputFileName);
				closeJobCommand(result.id,CloseJobRequest.UPLOADCOMPLETE);
				pollForResults(result.id);
			}
			break;
		case RESULTS:
			System.out.println(getResultsCommand(jobId, GetJobResults.RESULTKIND.SUCCESS,outputDir));
			System.out.println(getResultsCommand(jobId, GetJobResults.RESULTKIND.FAILED,outputDir));
			System.out.println(getResultsCommand(jobId, GetJobResults.RESULTKIND.UNPROCESSED,outputDir));
			break;
		case STATUS:
			System.out.println(getStatusCommand(jobId));
			break;
		case CLOSEJOB:
			System.out.println(closeJobCommand(jobId,CloseJobRequest.UPLOADCOMPLETE));
			break;
		case ABORTJOB:
			System.out.println(closeJobCommand(jobId,CloseJobRequest.ABORTED));
			break;
		case QUERY:
			System.out.println(createQueryCommand(objectName,queryString));	
			break;
		default:
			break;
		}

					
		
	}

	/**
	 * If pollingInterval is greater than zero, poll for this job to complete
	 * @param jobId
	 * @throws InterruptedException 
	 * @throws AuthenticationException 
	 * @throws IOException 
	 * @throws URISyntaxException 
	 * @throws ClientProtocolException 
	 */
	private JobInfo pollForResults(String jobId) throws InterruptedException, ClientProtocolException, URISyntaxException, IOException, AuthenticationException {
		JobInfo info=null;
		if (this.pollingInterval > 0) {
			// Loop until an exception or job completion

			GetJobInfo getter = new GetJobInfo(getInstanceUrl(),getAuthToken());

			do {
				_logger.info("sleeping " + pollingInterval + " seconds");
				Thread.sleep(pollingInterval * 1000);
				info = getter.execute(jobId); 
				_logger.info(info.toString());
			} while (info.isRunning());
			
			if (info!= null && info.isComplete()) {
				_logger.info(getResultsCommand(info.id, GetJobResults.RESULTKIND.SUCCESS,outputDir));
				_logger.info(getResultsCommand(info.id, GetJobResults.RESULTKIND.FAILED,outputDir));
				_logger.info(getResultsCommand(info.id, GetJobResults.RESULTKIND.UNPROCESSED,outputDir));
			} else {
				_logger.info(info == null ? "Unable to get job results " : info.toString());
			}

			
		}
		return info;
		
	}

	private String getResultsCommand(String jobId, GetJobResults.RESULTKIND kind, String outputDir) throws ClientProtocolException, URISyntaxException, IOException, AuthenticationException {
		return new GetJobResults(getInstanceUrl(),getAuthToken()).execute(jobId,kind,outputDir);
	}

	private void uploadFileOperation(String contentUrl, String inputFileName) throws URISyntaxException, ClientProtocolException, IOException, AuthenticationException {
		UploadJob upload = new UploadJob(getInstanceUrl(),getAuthToken());
		upload.execute(contentUrl,inputFileName);
	}

	private CreateJobResponse createJobCommand(String objectName, String operation, String externalIdFieldName) throws Throwable {
		CreateJob creator = new CreateJob(getInstanceUrl(),getAuthToken());
		return creator.execute(objectName,operation,externalIdFieldName);
	}
	
	private BulkV1JobResponse createQueryCommand(String objectName, String query) throws Throwable {
		
		CreateV1Job creator = new CreateV1Job(getInstanceUrl(),getAuthToken());
		BulkV1JobResponse result = creator.execute("query",objectName);
		
		// Create the Batch with the actual Query in it
		CreateV1Batch batcher = new CreateV1Batch(getInstanceUrl(),getAuthToken());
		batcher.execute(result.id,query);
		
		// Close the Job
		CloseV1Job jobCloser = new CloseV1Job(getInstanceUrl(),getAuthToken());
		BulkV1JobResponse closeJobResult = jobCloser.execute(result.id);
		return closeJobResult;
	}


	/**
	 * Get the Bulk API Jobs information
	 * 
	 * @throws Throwable 
	 */
	public GetAllJobsResponse listJobsCommand() throws Throwable {
		return new GetAllJobs(getInstanceUrl(),getAuthToken()).execute();
	}
	
	/**
	 * Get Bulk API Job Status information for a single Job
	 * 
	 * @throws Throwable 
	 */
	public JobInfo getStatusCommand(final String jobId) throws Throwable {
		return new GetJobInfo(getInstanceUrl(),getAuthToken()).execute(jobId);
	}
	
	/**
	 * Close an existing Bulk API V2 Job - either UploadCompleted or Aborted
	 * 
	 * @throws Throwable 
	 */
	public JobInfo closeJobCommand(String jobId, String subCommand) throws Throwable {
		return new CloseJob(getInstanceUrl(),getAuthToken()).execute(jobId, subCommand);
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
			
			
			if (flagPart.compareTo(Flags.JOBID.getLabel())==0) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Invalid JobID!"+valuePart);
				} else {
					this.jobId = valuePart;	
				}
			}
			
			if (flagPart.compareTo(Flags.LIST.getLabel())==0) {
				this.currentCommand=Commands.LIST;
			}
			
			if (flagPart.compareTo(Flags.STATUS.getLabel())==0) {
				this.currentCommand=Commands.STATUS;
			}
			if (flagPart.compareTo(Flags.CLOSEJOB.getLabel())==0) {
				this.currentCommand=Commands.CLOSEJOB;
			}
			if (flagPart.compareTo(Flags.ABORTJOB.getLabel())==0) {
				this.currentCommand=Commands.ABORTJOB;
			}
			
			if (flagPart.compareTo(Flags.INSERT.getLabel())==0) {
				this.currentCommand=Commands.INSERT;
			}
			if (flagPart.compareTo(Flags.DELETE.getLabel())==0) {
				this.currentCommand=Commands.DELETE;
			}

			if (flagPart.compareTo(Flags.RESULTS.getLabel())==0) {
				this.currentCommand=Commands.RESULTS;
			}
			
			if (flagPart.compareTo(Flags.OBJECTNAME.getLabel())==0) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing Objectname!");
				} else {
					this.objectName = valuePart;	
				}				
			}
			if (flagPart.compareTo(Flags.EXTERNALID.getLabel())==0) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing ExternalID fieldname!");
				} else {
					this.externalIdFieldName = valuePart;	
				}				
			}
			
			if (flagPart.compareTo(Flags.INPUTFILE.getLabel())==0) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing filename!");
				} else {
					this.inputFileName = valuePart;	
				}				
			}
			if (flagPart.compareTo(Flags.OUTPUTDIR.getLabel())==0) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing output directory name!");
				} else {
					this.outputDir = valuePart;	
				}				
			}
			if (flagPart.compareTo(Flags.POLL.getLabel())==0) {
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing polling interval!");
				} else {
					this.pollingInterval = Integer.parseInt(valuePart);
				}				
			}
			
			if (flagPart.compareTo(Flags.QUERY.getLabel())==0) {
				this.currentCommand=Commands.QUERY;
				
				if (valuePart.isEmpty()) {
					throw new IllegalArgumentException("Missing query string!");
				} else {
					this.queryString = valuePart;
				}				
			}


			
		}
		return;
	}

	/**
	 * Define the Commands this processor implements
	 * @author bobby.white
	 *
	 */
	public enum Commands {
		LIST,
		INSERT,
		UPSERT,
		DELETE,
		STATUS,
		CLOSEJOB,
		ABORTJOB,
		RESULTS,
		QUERY
	}
	
	public enum Flags {
		LIST("l","List Jobs"),
		INSERT("i","Insert Records"),
		UPSERT("u","Upsert Records"),
		DELETE("d","Delete Records"),
		STATUS("s","Get Job Status"),
		CLOSEJOB("c","Close Job"),
		ABORTJOB("a","Abort Job"),
		RESULTS("r","Get Job results"),
		JOBID("j","Hex ID of the Job",true), 
		INPUTFILE("f","Input filename",true), 
		OUTPUTDIR("D","Output Directory",true),
		EXTERNALID("x","External ID fieldname for Upsert",true),
		OBJECTNAME("o","Object name for Insert, Update, or Delete",true),
		POLL("p","Poll for results - interval in seconds",true),
		QUERY("q","SOQL Query string",true);
		
		final private String label;
		final private String description;
		final private boolean requiresValue;

		Flags(String label, String description) {
		this.label=label;
		this.description=description;
		this.requiresValue=false;
		}
		Flags(String label, String description, boolean requiresValue) {
		this.label=label;
		this.description=description;
		this.requiresValue=requiresValue;
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
		public boolean getRequiresValue() {
			return requiresValue;
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
	public static String getURLFromURLTemplate(
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
