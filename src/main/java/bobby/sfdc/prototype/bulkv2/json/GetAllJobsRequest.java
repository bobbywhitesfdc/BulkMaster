package bobby.sfdc.prototype.bulkv2.json;
/**
	Optional Request Body for GetAllJobs call to list Bulk Jobs
**/
public class GetAllJobsRequest {
   boolean isPkChunkingEnabled; // filter
   String jobType;	// BigObjectIngest, Classic, V2Ingest
   String queryLocator;
}