package bobby.sfdc.prototype.rest;

import bobby.sfdc.prototype.BulkMaster;

public abstract class AbstractAPIBase {

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
	
	protected final String getURLFromURLTemplate(final String resource, final String paramName, final String paramValue) {
		return BulkMaster.getURLFromURLTemplate(resource,paramName,paramValue);
	}

}