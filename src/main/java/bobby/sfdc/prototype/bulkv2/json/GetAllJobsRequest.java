package bobby.sfdc.prototype.bulkv2.json;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**
	Optional Request Body for GetAllJobs call to list Bulk Jobs
**/
public class GetAllJobsRequest extends AbstractJSONBody {
   boolean isPkChunkingEnabled; // filter
   String jobType;	// BigObjectIngest, Classic, V2Ingest
   String queryLocator;
}