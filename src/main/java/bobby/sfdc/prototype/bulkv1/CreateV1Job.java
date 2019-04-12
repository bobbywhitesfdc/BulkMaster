package bobby.sfdc.prototype.bulkv1;

import bobby.sfdc.prototype.rest.AbstractAPIBase;

public class CreateV1Job extends AbstractAPIBase {
	public static final String RESOURCE = "/services/async/V45.0/job";

	public CreateV1Job(String instanceUrl, String authToken) {
		super(instanceUrl, authToken);
	}
	public String execute(String operation, String objectName) {
		return "";
	}

}
