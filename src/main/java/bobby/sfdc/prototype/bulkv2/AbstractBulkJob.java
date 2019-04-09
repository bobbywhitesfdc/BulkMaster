package bobby.sfdc.prototype.bulkv2;

import bobby.sfdc.prototype.BulkMaster;

public abstract class AbstractBulkJob {

	protected final String authToken;
	protected final String instanceUrl;

	public AbstractBulkJob(String instanceUrl, String authToken) {
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