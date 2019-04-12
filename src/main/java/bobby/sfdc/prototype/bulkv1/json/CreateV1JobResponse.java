package bobby.sfdc.prototype.bulkv1.json;

/**
 * Create Bulk API V1 Job Response JSON Body
 * @author bobby.white
 * 
 * {
   "apexProcessingTime" : 0,
   "apiActiveProcessingTime" : 0,
   "apiVersion" : 36.0,
   "concurrencyMode" : "Parallel",
   "contentType" : "JSON",
   "createdById" : "005D0000001b0fFIAQ",
   "createdDate" : "2015-12-15T20:45:25.000+0000",
   "id" : "750D00000004SkGIAU",
   "numberBatchesCompleted" : 0,
   "numberBatchesFailed" : 0,
   "numberBatchesInProgress" : 0,
   "numberBatchesQueued" : 0,
   "numberBatchesTotal" : 0,
   "numberRecordsFailed" : 0,
   "numberRecordsProcessed" : 0,
   "numberRetries" : 0,
   "object" : "Account",
   "operation" : "insert",
   "state" : "Open",
   "systemModstamp" : "2015-12-15T20:45:25.000+0000",
   "totalProcessingTime" : 0
}
 *
 */
public class CreateV1JobResponse {
	   public int apexProcessingTime;
	   public String concurrencyMode;
	   public String id;
	   public String object;
	   public String operation;
	   public String state;
	   public String systemModstamp;
	   public String createdById;
	   public String createdDate;
}
