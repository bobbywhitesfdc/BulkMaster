package bobby.sfdc.prototype.bulkv2.json;

import com.google.gson.Gson;

/**
 * Request Parameters to Close or Abort a Job
 * @author bobby.white
 *
 */
public class CloseJobRequest {
	public static final String UPLOADCOMPLETE = "UploadComplete";
	public static final String ABORTED = "Aborted";
	
	public String state;

	public String toJson() {
		String retValue = new Gson().toJson(this);
		return retValue;
	}
}

