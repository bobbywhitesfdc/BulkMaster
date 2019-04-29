package bobby.sfdc.prototype.bulkv2.json;

import java.util.ArrayList;
import java.util.List;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**
  Response to GetAllJobs API call
**/
public class GetAllJobsResponse extends AbstractJSONBody {
	public boolean done;
	public JobInfo[] records;
	public String nextRecordsUrl;
	// Computed summary fields
	public int runningJobs=0;
	public int completedJobs=0;
	/**
	 * Optionally Filter the summary/print by objectName and Operation
	 * @param objectName
	 * @param operation
	 */
	public void filter(String objectName, String operation) {

		// Remove all of the jobs that don't match this object
		List<JobInfo> onesToKeep = new ArrayList<JobInfo>();
		for (JobInfo current : records) {
			if ((objectName == null || current.object.compareTo(objectName)==0) 
				&& operation == null || current.operation.compareTo(operation)==0) {
				onesToKeep.add(current);	
			}
		}
		records = (JobInfo[])onesToKeep.toArray(new JobInfo[onesToKeep.size()]);

	}
	public void summarize() {
		runningJobs=0;
		completedJobs=0;

		for (JobInfo current : records) {
			if (current.isRunning()) runningJobs++;
			if (current.isComplete()) completedJobs++;
		}
	}
}