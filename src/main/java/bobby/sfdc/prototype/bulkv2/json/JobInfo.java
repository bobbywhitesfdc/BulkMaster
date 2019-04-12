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
	String id;
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

}