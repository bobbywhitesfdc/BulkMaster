package bobby.sfdc.prototype.bulkv2.json;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**

JobInfo - response from GetAllJobs

**/

public class JobInfo extends AbstractJSONBody {
    String apiVersion;
    String concurrencyMode;
    String contentType;
    String contentUrl;
    String jobType;
    String createdById;
    String createdDate;
	public String id;
	String lineEnding;
	String state;
	String systemModstamp;
	String object;
	String operation;
	int retries;
	public int numberRecordsFailed;
	public int numberRecordsProcessed;
	long totalProcessingTime;

	/** Convenience Method to determine if the job is in a running State 
	 * 
	 * @return true if the Job is running
	 */
	public boolean isRunning() {
		return "UploadComplete".compareTo(state)==0 || "InProgress".compareTo(state)==0;
	}
	public boolean isComplete() {
		return "JobComplete".compareTo(state)==0;
	}
	public boolean isQueued() {
		return "UploadComplete".compareTo(state)==0;
	}

}