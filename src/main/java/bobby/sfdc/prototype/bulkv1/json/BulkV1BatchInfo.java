package bobby.sfdc.prototype.bulkv1.json;

import javax.xml.bind.annotation.XmlRootElement;

import bobby.sfdc.prototype.rest.AbstractJSONBody;

/**
 * 
 * @author bobby.white
 *
<?xml version="1.0" encoding="UTF-8"?>
<batchInfo
    xmlns="http://www.force.com/2009/06/asyncapi/dataload">
  <id>751x000000009vwAAA</id>
  <jobId>750x000000009tvAAA</jobId>
  <state>Not Processed</state>
  <createdDate>2016-01-10T00:59:47.000Z</createdDate>
  <systemModstamp>2016-01-10T01:00:19.000Z</systemModstamp>
  <numberRecordsProcessed>0</numberRecordsProcessed>
  <numberRecordsFailed>0</numberRecordsFailed>
  <totalProcessingTime>0</totalProcessingTime>
  <apiActiveProcessingTime>0</apiActiveProcessingTime>
  <apexProcessingTime>0</apexProcessingTime>
</batchInfo>
 */

@XmlRootElement(name="batchInfo")
public class BulkV1BatchInfo extends AbstractJSONBody {
	public String id;
	public String jobId;
	public String state;
	public String createdDate;
	public String systemModstamp;
	public int numberRecordsProcessed;
	public int numberRecordsFailed;
	public int totalProcessingTime;
	public int apiActiveProcessingTime;
	public int apexProcessingTime;
	/**
	 * <simpleType name="BatchStateEnum">
		<restriction base="string">
		<enumeration value="Queued"/>
		<enumeration value="InProgress"/>
		<enumeration value="Completed"/>
		<enumeration value="Failed"/>
		<enumeration value="NotProcessed"/>
		</restriction>
		</simpleType>
	 * @return
	 */
	public boolean isRunning() {
		return ( state.compareTo("InProgress")==0 || state.compareTo("Queued")==0);
	}
}
