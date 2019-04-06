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
		builder.append(" systemModstamp=");
		builder.append(systemModstamp);

		return builder.toString();
	}

}