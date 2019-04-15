package bobby.sfdc.prototype.bulkv1.json;

import javax.xml.bind.annotation.XmlRootElement;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**
 * 
 * @author bobby.white
 *
 *{
   "apexProcessingTime":0,
   "apiActiveProcessingTime":0,
   "createdDate":"2015-12-15T21:56:43.000+0000",
   "id":"751D00000004YGZIA2",
   "jobId":"750D00000004SkVIAU",
   "numberRecordsFailed":0,
   "numberRecordsProcessed":0,
   "state":"Queued",
   "systemModstamp":"2015-12-15T21:56:43.000+0000",
   "totalProcessingTime":0
}
 */

@XmlRootElement(name="batchInfo")
public class CreateV1BatchResponse extends AbstractJSONBody {
	public int apexProcessingTime;
	public int apiActiveProcessingTime;
	public String createdDate;
	public String id;
	public String jobId;
	public int numberRecordsFailed;
	public int numberRecordsProcessed;
	public String state;
	public String systemModstamp;
}
