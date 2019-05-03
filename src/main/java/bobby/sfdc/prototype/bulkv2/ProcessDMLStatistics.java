package bobby.sfdc.prototype.bulkv2;

import bobby.sfdc.prototype.bulkv2.json.JobInfo;
import bobby.sfdc.prototype.rest.AbstractJSONBody;

public class ProcessDMLStatistics extends AbstractJSONBody {
	public int totalNumberOfJobs=0;
	public int numberOfQueuedJobs=0;
	public int numberOfCompletedJobs=0;
	public int totalNumberOfRecords=0;
	public int recordsProcessed=0;
	public int recordsFailed=0;
	public double percentComplete=0.0;
	
	
	public ProcessDMLStatistics(int recordsToBeProcessed) {
		totalNumberOfRecords = recordsToBeProcessed;
	}
	
	/**
	 * Update the overall statistics to include this job, updating tallies each time
	 * 
	 * @param currentJob
	 */
	public void addJobInfo(JobInfo currentJob) {
		++totalNumberOfJobs;
		if (currentJob.isQueued()) ++numberOfQueuedJobs;
		if (currentJob.isComplete()) ++numberOfCompletedJobs;
		recordsProcessed += currentJob.numberRecordsProcessed;
		recordsFailed += currentJob.numberRecordsFailed;
		percentComplete = totalNumberOfRecords > 0 ? (recordsProcessed + recordsFailed) / totalNumberOfRecords : 0.0;
	}

}
