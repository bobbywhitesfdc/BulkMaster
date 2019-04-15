package bobby.sfdc.prototype.bulkv2.json;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**
 * Request Parameters to Close or Abort a Job
 * @author bobby.white
 *
 */
public class CloseJobRequest extends AbstractJSONBody {
	public static final String UPLOADCOMPLETE = "UploadComplete";
	public static final String ABORTED = "Aborted";
	
	public String state;
}

