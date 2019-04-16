package bobby.sfdc.prototype.bulkv1.json;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

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
public class BulkV1JobResponse  extends AbstractJSONBody{
	   public String id;
	   public String object;
	   public String operation;
	   public String state;
	   public int apexProcessingTime;
	   public int apiActiveProcessingTime;
	   public String apiVersion;
	   public String concurrencyMode;
	   public String contentType;
	   public int numberBatchesCompleted;
	   public int numberBatchesFailed;
	   public int numberBatchesInProgress;
	   public int numberBatchesQueued;
	   public int numberBatchesTotal;
	   public int numberRecordsFailed;
	   public int numberRecordsProcessed;
	   public int numberRetries;
	   public String systemModstamp;
	   public String createdById;
	   public String createdDate;
	   public int totalProcessingTime;
}
