package bobby.sfdc.prototype.bulkv2.json;
/**
  Response to GetAllJobs API call
**/
public class GetAllJobsResponse {
	boolean done;
	JobInfo[] records;
	String nextRecordsUrl;

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("done=");
		builder.append(done);
		if (nextRecordsUrl != null) {
			builder.append(", ");
			builder.append("nextRecordsUrl=");
			builder.append(nextRecordsUrl);
		}
		if (records != null) {
			builder.append("[\n");
			for(JobInfo current : records) {
				builder.append(current);
				builder.append('\n');
			}
			builder.append("]");
		}
		return builder.toString();
	}
	
}