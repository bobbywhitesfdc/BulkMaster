package bobby.sfdc.prototype.bulkv1.json;

/**
 * JSON Request Structure used to Create a Bulk API V1 Job
 * @author bobby.white
 *
 *	{
		  "operation" : "insert",
		  "object" : "Account",
		  "contentType" : "CSV"
	}
 */
public class CreateV1JobRequest {
	public String operation;
	public String object;
	public String contentType;
	public String concurrencyMode;
}
