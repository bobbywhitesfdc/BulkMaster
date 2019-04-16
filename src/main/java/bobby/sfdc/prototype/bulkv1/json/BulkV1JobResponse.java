package bobby.sfdc.prototype.bulkv1.json;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**
 * Create Bulk API V1 Job Response JSON Body
 * @author bobby.white
 * 
 *{"apexProcessingTime":0
  ,"apiActiveProcessingTime":0
  ,"apiVersion":45.0
  ,"assignmentRuleId":null
  ,"concurrencyMode":"Parallel"
  ,"contentType":"CSV"
  ,"createdById":"00541000000FJJ6AAO"
  ,"createdDate":"2019-04-16T12:20:33.000+0000"
  ,"externalIdFieldName":null
  ,"fastPathEnabled":false
  ,"id":"7502M00000Sp9PYQAZ"
  ,"numberBatchesCompleted":1
  ,"numberBatchesFailed":0
  ,"numberBatchesInProgress":0
  ,"numberBatchesQueued":0
  ,"numberBatchesTotal":1
  ,"numberRecordsFailed":0
  ,"numberRecordsProcessed":664280
  ,"numberRetries":0,"object":"Lead"
  ,"operation":"query"
  ,"state":"Closed"
  ,"systemModstamp":"2019-04-16T12:20:33.000+0000"
  ,"totalProcessingTime":0
 *}
 *
 */
public class BulkV1JobResponse  extends AbstractJSONBody{
	   public String id;
	   public String object;
	   public String operation;
	   public String state;
	   public String externalIdFieldName;
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
