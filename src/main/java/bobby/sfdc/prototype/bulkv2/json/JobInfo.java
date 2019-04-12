package bobby.sfdc.prototype.bulkv2.json;
/**

JobInfo - response from GetAllJobs

**/

public class JobInfo {
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
	int numberRecordsFailed;
	int numberRecordsProcessed;
	long totalProcessingTime;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("id=");
		builder.append(id);
		builder.append(" jobType=");
		builder.append(jobType);
		builder.append(" state=");
		builder.append(state);
		builder.append(" concurrencyMode=");
		builder.append(concurrencyMode);
		builder.append(" contentUrl=");
		builder.append(contentUrl);
		builder.append(" lineEnding=");
		builder.append(lineEnding);
		builder.append(" systemModstamp=");
		builder.append(systemModstamp);

		return builder.toString();
	}

	/** Convenience Method to determin if the job is in a running State 
	 * 
	 * @return true if the Job is running
	 */
	public boolean isRunning() {
		return "UploadComplete".compareTo(state)==0 || "InProcess".compareTo(state)==0;
	}
	public boolean isComplete() {
		return "JobComplete".compareTo(state)==0;
	}

}